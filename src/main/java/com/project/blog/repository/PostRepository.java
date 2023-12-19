package com.project.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByPublished(boolean published, Pageable pageable);

    Page<Post> findByTitleContaining(String title, Pageable pageable);
}
