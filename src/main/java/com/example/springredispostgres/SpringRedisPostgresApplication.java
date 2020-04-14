package com.example.springredispostgres;

import com.example.springredispostgres.model.Author;
import com.example.springredispostgres.model.Post;
import com.example.springredispostgres.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;

@EnableCaching
@RequiredArgsConstructor
@SpringBootApplication
public class SpringRedisPostgresApplication {

    private final PostRepository postRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringRedisPostgresApplication.class, args);
    }

    @PostConstruct
    public void initDatabase() {
        postRepository.save(new Post("Cyberpunk is near", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 555, new Author("Anna")));
        postRepository.save(new Post("Welcome aboard of the hype train", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 55, new Author("Josh")));
        postRepository.save(new Post("How to improve programming skills ", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 555, new Author("Kobe")));
        postRepository.save(new Post("Top exercises for IT people", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 55, new Author("Leo")));
        postRepository.save(new Post("Case study of 75 years project", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 55, new Author("Tom")));
        postRepository.save(new Post("Machine Learning", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 255, new Author("Alexa")));
        postRepository.save(new Post("Memory leaks, how to find them ", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 55, new Author("Frank")));
        postRepository.save(new Post("Robots builds robots", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 155, new Author("Milagros")));
        postRepository.save(new Post("Quantum algorithms, from the scratch", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 455, new Author("Sarah")));
        postRepository.save(new Post("Coding, coding, coding", "Description", "https://random-cdn.posts/images/xvn84934fnls.jpg", 255, new Author("Adam")));
    }

}
