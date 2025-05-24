package com.example.socialmedia.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentRequest {
    
    @NotBlank
    private String content;
}