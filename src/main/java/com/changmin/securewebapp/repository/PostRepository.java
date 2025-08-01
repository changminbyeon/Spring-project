package com.changmin.securewebapp.repository;

import com.changmin.securewebapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
}