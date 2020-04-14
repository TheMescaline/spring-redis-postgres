package com.example.springredispostgres.service;

import com.example.springredispostgres.exceptions.PostNotFoundException;
import com.example.springredispostgres.model.Author;
import com.example.springredispostgres.model.Post;
import com.example.springredispostgres.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post getPostByID(Long id) throws PostNotFoundException {
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
    public List<Post> getTopPosts() {
        // иммитация highload database
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return postRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public void updatePost(Post post) throws PostNotFoundException {
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) throws PostNotFoundException {
        postRepository.deleteById(postId);
    }
}

