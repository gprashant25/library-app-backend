package com.luv2code.spring_boot_library.dao;

import com.luv2code.spring_boot_library.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // here we're going to add our Message repository so we can find all messages by a user.
    // this is going to add Pagination and be able to search by userEmail to find all messages
    // frontend react url example const url = `http://localhost:8080/api/messages/search/findByUserEmail?userEmail=${authState?.accessToken?.claims.sub}&page=${currentPage - 1}&size=${messagesPerPage}`;
    Page<Message> findByUserEmail(@RequestParam("user_email")String userEmail, Pageable pageable);

    // Please Note: the normal user can see all the messages that has been responded or not
    // But, for the ADMIN USER will only want to see questions that have not been responded to.
    // hence creating the new endpoint that our admins are going to be calling which doesn't really need to have admin restrictions. so we are finding the messages by all users and not only finding for specific users. so this is going to be all users who currently have an open ticket or message to our admin.
    Page<Message> findByClosed(@RequestParam("closed")boolean closed, Pageable pageable);

}
