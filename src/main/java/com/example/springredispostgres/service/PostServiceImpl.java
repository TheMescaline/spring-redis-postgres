package com.example.springredispostgres.service;

import com.example.springredispostgres.exceptions.PostNotFoundException;
import com.example.springredispostgres.model.Author;
import com.example.springredispostgres.model.Post;
import com.example.springredispostgres.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    // проверяем, что при изменении сущности в каком-то методе, кеш в redis не меняется
    public Post changePostByID(Long id) throws PostNotFoundException {
        log.info("===> FROM METHOD:   change post with id {}", id);
        Post post= postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Cannot find post with id:" + id));
        post.setTitle("*****");
        post.setDescription("*****");
        post.setShares(1000);
        post.setImage("*****");
        post.setAuthor(null);
        postRepository.save(post);
        return getPostByID(id);
    }

    @Override
    @Cacheable(value = "post-single", key = "#id", unless = "#result.shares < 500")
    // кеширует только посты с shares > 500 (posts 1, 3)
    public Post getPostByID(Long id) throws PostNotFoundException {
        log.info("===> FROM METHOD:   get post with id {}", id);
        // иммитация highload database
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Cannot find post with id:" + id));
    }


    @Override
    @Cacheable(value = "post-top")
    // кеширует все посты
    public List<Post> getTopPosts() {
        log.info("===> FROM METHOD:   get top posts");
        // иммитация highload database
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return postRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
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
    public Post updatePost(Post post) throws PostNotFoundException {
        log.info("===> FROM METHOD:   update post with id {}", post.getId());
        return postRepository.save(post);
    }

    @Override
    @CacheEvict(value = "post-single", key = "#postId")
    // удаление записей из кеша по условию (id)
    public void deletePost(Long postId) throws PostNotFoundException {
        log.info("===> FROM METHOD:   delete post with id {}", postId);
        postRepository.deleteById(postId);
    }

    @Override
    @CacheEvict(value = "post-top", allEntries = true)
    // удаление всех записей из кеша
    public void deleteCacheAllPosts() {
        log.info("===> FROM METHOD:   Evict post-top");
    }

}

