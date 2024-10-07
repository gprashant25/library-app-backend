package com.luv2code.spring_boot_library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity                   //java objects Entities are mapped to database tables
@Table(name = "book")
@Data                      // @Data is the lombok annotation which is going to dynamically create all the getters and setters methods automatically for book.
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "copies")
    private int copies;

    @Column(name = "copies_available")
    private int copiesAvailable;

    @Column(name = "category")
    private String category;

    @Column(name = "img")
    private String img;


}
