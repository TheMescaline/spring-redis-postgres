package com.example.springredispostgres.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "posts")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String image;
    private int shares;
    private Author author;

    public Post(String title, String description, String image, int shares, Author author) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.shares = shares;
        this.author = author;
    }
}
