package com.luv2code.spring_boot_library.service;

import com.luv2code.spring_boot_library.dao.BookRepository;
import com.luv2code.spring_boot_library.dao.CheckoutRepository;
import com.luv2code.spring_boot_library.dao.HistoryRepository;
import com.luv2code.spring_boot_library.entity.Book;
import com.luv2code.spring_boot_library.entity.Checkout;
import com.luv2code.spring_boot_library.entity.History;
import com.luv2code.spring_boot_library.responsemodels.ShelfCurrentLoansResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional   // used to manage the transactions declaratively. Transaction are critical aspect of any application that interact with the database or any other transactional resource.
public class BookService {

    // below creating instance variable for using a constructor Dependency injection
    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    private HistoryRepository historyRepository;

    // constructor dependency injection
    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository, HistoryRepository historyRepository){
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository = historyRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception{

        // when you call a database, we return an optional of the entity
        Optional<Book> book = bookRepository.findById(bookId);

        // here below we're trying to validate that checkout == null bcos if its not null that means we found a book in the database where the userEmail and bookId matches
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(!book.isPresent() || validateCheckout !=null || book.get().getCopiesAvailable() <=0){
            throw new Exception("Book doesn't exist or already checked out by the user.");
        }

        // here so out of all the available copies of this book, we just want to subtract one
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);

        // to update into the database
        bookRepository.save(book.get());

        //create a new checkout object that we can save to the database as a new record.
        Checkout checkout = new Checkout(
                userEmail,
                LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(),
                book.get().getId()
        );

        checkoutRepository.save(checkout);

        return book.get();

    }


    // below code to verify that if this book is checked out by the user or not.
    public Boolean checkoutBookByUser(String userEmail, Long bookId){

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(validateCheckout !=null){
            return true;   // this means book is checked out bcos there is an item found in validateCheckout
        } else {
            return false;
        }
    }

    public int currentLoansCount(String userEmail){

        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }


    // to display the Current loans shelf part
    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {

        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        // here we're going to get a list of all the books that the user currently has checked out. so its only going to give us a bookId
        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);

        // here all the bookIds will be added from the checkedout list
        List<Long> bookIdList = new ArrayList<>();

        // below we want to extract all the bookIds out of the Checkout list
        for(Checkout i : checkoutList){
            bookIdList.add(i.getBookId());
        }

        // creating a new list of books from the checkedout books list we got
        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        // Its a concrete class for formatting and parsing dates in a local sensitive manner. Its going to compare dates to see how many days left until this book need to be returned or how late the book is.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(Book book : books){
            // below code means when we call this checkout list, we are not sure if this is actually going to be real item. so we're going to wrap it in optional.
            // below code is to find the match between checkoutList and our Book both with the same matching bookId
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId() == book.getId())   //here filter which returns a stream consisting of the elements of the stream that matches that given predicate. here x is the each item in our checkoutList and comapring with our Book
                    .findFirst();

            if(checkout.isPresent()){

                Date d1 = sdf.parse(checkout.get().getReturnDate());   // return of the book date
                Date d2 = sdf.parse(LocalDate.now().toString());  // today's date

                // to check what is the difference between these 2 dates by day?
                TimeUnit time = TimeUnit.DAYS;

                long difference_In_Time = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) difference_In_Time));
            }
        }

        return shelfCurrentLoansResponses;
    }


    // React manage Loan - Return Book service and saving the book details into the history database
    public void returnBook(String userEmail, Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        // below we're just verifying that in the database within our Checkout table that we do have a record that has our userEmail with the specific bookId.
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(!book.isPresent() || validateCheckout == null){
            throw new Exception("Book does not exist or not checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        bookRepository.save(book.get());     // saving the new book

        // here we're deleting the checkedOut bookId from the Checkout database table
        checkoutRepository.deleteById(validateCheckout.getId());

        // here we create a new History object and then save that object to our history repository
        // here we need to save a new History object to our database  - this operation is performed after the using is returning the book everytime
        History history = new History(
                userEmail,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),   // returned date would be the todays date
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );

        historyRepository.save(history);   // here saving the history oft the returned book into our history repository ie our in our database
    }


    // Renew Loan Service
    public void renewLoan(String userEmail, Long bookId) throws Exception {

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if(validateCheckout == null){
            throw new Exception("Book does not exist or not checkedout by the user");
        }

        // now here below we wanted to create another simple date formatter bcos we need to do some kind of conditional check to make sure that this book is in fact not past the due date.
        // Bcos we dont want the ability to renew a book if its past the due date and we want ability only for the books that are not already late.

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = sdFormat.parse(validateCheckout.getReturnDate());
        Date d2 = sdFormat.parse((LocalDate.now().toString()));

        if(d1.compareTo(d2) > 0 || d1.compareTo(d2) == 0){

            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
            checkoutRepository.save(validateCheckout);
        }


    }


}
