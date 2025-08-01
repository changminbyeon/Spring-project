package com.changmin.securewebapp.service;

import com.changmin.securewebapp.dto.PostRequestDto;
import com.changmin.securewebapp.dto.PostResponseDto;
import com.changmin.securewebapp.dto.PostSummaryDto;
import com.changmin.securewebapp.entity.Post;
import com.changmin.securewebapp.exception.ResourceNotFoundException;
import com.changmin.securewebapp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

        private final PostRepository postRepository;

        @Transactional(readOnly = true)
        public PostResponseDto getPost(Long postId){
                Post post = postRepository.findById(postId)
                        .orElseThrow(()->new ResourceNotFoundException("해당 게시글을 찾을 수 없음."));
                return new PostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthor(),
                        post.getCreatedAt().toString()
                );
        }

        @Transactional
        public PostResponseDto createPost(PostRequestDto dto, String username) {
                Post post = Post.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .author(username)
                        .createdAt(LocalDateTime.now())
                        .build();

                Post saved = postRepository.save(post);

                return new PostResponseDto(
                        saved.getId(),
                        saved.getTitle(),
                        saved.getContent(),
                        saved.getAuthor(),
                        saved.getCreatedAt().toString()
                );
        }

        @Transactional
        public PostResponseDto updatePost(Long id, PostRequestDto dto) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String currentUsername = authentication.getName();

                Post post = postRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없음!"));

                if (!post.getAuthor().equals(currentUsername)) {
                        throw new AccessDeniedException("게시글 수정 권한 없음!");
                }

                post.setTitle(dto.getTitle());
                post.setContent(dto.getContent());
                postRepository.save(post);

                return new PostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthor(),
                        post.getCreatedAt().toString()
                );
        }

        @Transactional
        public void deletePost(Long id) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String currentUsername = authentication.getName();

                Post post = postRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

                if (!post.getAuthor().equals(currentUsername)) {
                        throw new AccessDeniedException("게시글 삭제 권한 없음.");
                }

                postRepository.delete(post);
        }

        @Transactional(readOnly = true)
        public List<PostSummaryDto> getAllPosts() {
                return postRepository.findAll().stream()
                        .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt())) // 최신순 정렬
                        .map(post -> new PostSummaryDto(
                                post.getId(),
                                post.getTitle(),
                                post.getAuthor(),
                                post.getCreatedAt().toString()
                        ))
                        .collect(Collectors.toList());
        }
}
