package com.example.springredispostgres.service;

import com.example.springredispostgres.exceptions.PostNotFoundException;
import com.example.springredispostgres.model.Post;

import java.util.List;

public interface PostService {

    Post getPostByID(Long id) throws PostNotFoundException;
    List<Post> getTopPosts();
    Post updatePost(Post post) throws PostNotFoundException;
    void deletePost(Long postID) throws PostNotFoundException;
    void deleteCacheAllPosts();

    Post changePostByID(Long id) throws PostNotFoundException;
}
