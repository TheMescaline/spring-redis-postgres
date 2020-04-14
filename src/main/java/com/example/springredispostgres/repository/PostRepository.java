package com.example.springredispostgres.repository;

import com.example.springredispostgres.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
