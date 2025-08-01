package com.changmin.securewebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
}
