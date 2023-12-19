package com.project.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.blog.model.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long>{

    List <Post> findByPublished(boolean published);
    List <Post> findByTitleContaining(String title);
}
