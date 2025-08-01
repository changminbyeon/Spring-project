package com.changmin.securewebapp.controller;

import com.changmin.securewebapp.dto.PostRequestDto;
import com.changmin.securewebapp.dto.PostResponseDto;
import com.changmin.securewebapp.dto.PostSummaryDto;
import com.changmin.securewebapp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return ResponseEntity.ok(postService.createPost(dto, username));
    }

    @GetMapping
    public ResponseEntity<List<PostSummaryDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody (required = false) PostRequestDto dto
    ) {
        if(dto == null){
            throw new IllegalArgumentException("요청 데이터 없음");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.updatePost(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        postService.deletePost(id);
        return ResponseEntity.ok("삭제되었습니다.");
    }

}
