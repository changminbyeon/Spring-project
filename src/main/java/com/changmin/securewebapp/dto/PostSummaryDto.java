package com.changmin.securewebapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostSummaryDto {
	private Long id;
	private String title;
	private String author;
	private String createdAt;
}
