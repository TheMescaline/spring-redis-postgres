package com.example.springredispostgres.service;

import com.example.springredispostgres.exceptions.PostNotFoundException;
import com.example.springredispostgres.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public interface PostService {

    Post getPostByID(Long id) throws PostNotFoundException;
    List<Post> getTopPosts();
    void updatePost(Post post) throws PostNotFoundException;
    void deletePost(Long postID) throws PostNotFoundException;

}
