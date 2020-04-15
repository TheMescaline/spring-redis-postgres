package com.example.springredispostgres.controller;

import com.example.springredispostgres.exceptions.PostNotFoundException;
import com.example.springredispostgres.model.Post;
import com.example.springredispostgres.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

//    @Autowired private ApplicationContext applicationContext;

    @GetMapping(value = "change/{id}", produces = "application/json;charset=UTF-8")
    public Post changePostByID(@PathVariable Long id) throws PostNotFoundException {
        return postService.changePostByID(id);
    }

    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    public Post getPostByID(@PathVariable Long id) throws PostNotFoundException {
        return postService.getPostByID(id);
    }

    @GetMapping("/top")
    public List<Post> getTopPosts() {
        return postService.getTopPosts();
    }

    @PutMapping("/update")
    public Post updatePostByID(@RequestBody Post post) throws PostNotFoundException {
        return postService.updatePost(post);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePostByID(@PathVariable Long id) throws PostNotFoundException {
        postService.deletePost(id);
    }

    @DeleteMapping("/top/evict")
    public void evictTopPosts() {
        postService.deleteCacheAllPosts();
    }

}
