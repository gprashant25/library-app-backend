package com.luv2code.spring_boot_library.service;

import com.luv2code.spring_boot_library.dao.MessageRepository;
import com.luv2code.spring_boot_library.entity.Message;
import com.luv2code.spring_boot_library.requestmodels.AdminQuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MessagesService {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    // below function for post a message: here we're receiving the message request and our email
    public void postMessage(Message messageRequest, String userEmail){

        // creating a new message and setting our userEmail and our title and question and then saving this to our database
        Message message = new Message(messageRequest.getTitle(), messageRequest.getQuestion());
        message.setUserEmail(userEmail);

        messageRepository.save(message);  // saving the new created message into our database
    }

    // here we would be able to do a put request to be able to update a current message with our admin response
    public void putMessage(AdminQuestionRequest adminQuestionRequest, String userEmail) throws Exception {

        // this is going to search for our database for a message that has this id. so if the message is not present then we would throw a exception
        Optional<Message> message = messageRepository.findById(adminQuestionRequest.getId());

        if(!message.isPresent()) {
            throw new Exception("Message not found");
        }

        message.get().setAdminEmail(userEmail);
        message.get().setResponse(adminQuestionRequest.getResponse());
        message.get().setClosed(true);

        messageRepository.save(message.get());
    }

}
