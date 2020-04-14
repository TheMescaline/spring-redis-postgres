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

    @Autowired private ApplicationContext applicationContext;

    @GetMapping(value = "/{id}", produces = "application/json;charset=UTF-8")
    @Cacheable(value = "post-single", key = "#id", unless = "#result.shares < 500")
    // кеширует только посты с shares > 500 (posts 1, 3)
    public Post getPostByID(@PathVariable Long id) throws PostNotFoundException {
        log.info("===> FROM METHOD:   get post with id {}", id);
        return postService.getPostByID(id);
    }

    @GetMapping("/top")
    @Cacheable(value = "post-top")
    // кеширует все посты
    public List<Post> getTopPosts() {
        log.info("===> FROM METHOD:   get top posts");
        return postService.getTopPosts();
    }

    @PutMapping("/update")
    @Caching(
            put = {
                    @CachePut(value = "post-single", key = "#post.id")
            },
            evict = {
                    @CacheEvict(value = "post-top", allEntries = true)
            }
    )
    // обновляет пост в базе и возвращает его поставщику кеша, чтобы изменить пост в кеше на новое значение
    // и очищаем кеш post-top, поскольку он уже не актуальный
    public Post updatePostByID(@RequestBody Post post) throws PostNotFoundException {
        log.info("===> FROM METHOD:   update post with id {}", post.getId());
        postService.updatePost(post);
        return post;
    }

    @DeleteMapping("/delete/{id}")
    @CacheEvict(value = "post-single", key = "#id")
    // удаление записей из кеша по условию (id)
    public void deletePostByID(@PathVariable Long id) throws PostNotFoundException {
        log.info("===> FROM METHOD:   delete post with id {}", id);
        postService.deletePost(id);
    }

    @DeleteMapping("/top/evict")
    @CacheEvict(value = "post-top", allEntries = true)
    // удаление всех записей из кеша
    public void evictTopPosts() {
        log.info("===> FROM METHOD:   Evict post-top");
    }

}
