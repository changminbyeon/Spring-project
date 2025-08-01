package com.changmin.securewebapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostRequestDto {
	private String title;
	private String content;
}