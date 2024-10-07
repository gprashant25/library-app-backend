package com.luv2code.spring_boot_library.entity;

// here we're going to be handling this through API endpoints called Messages in the Library Services page is going to be a way for non Admin users to be able to communicate with Admin users by sending messages.

import jakarta.persistence.*;
import lombok.Data;

@Entity  //java objects Entities are mapped to database tables
@Table(name = "messages")
@Data
public class Message {

    public Message(){

    }

    // parameterized constructor
    public Message(String title, String question){
        this.title = title;
        this.question = question;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "title")
    private String title;

    @Column(name = "question")
    private String question;

    @Column(name = "admin_email")
    private String adminEmail;

    @Column(name = "response")
    private String response;

    @Column(name = "closed")
    private boolean closed;


}
