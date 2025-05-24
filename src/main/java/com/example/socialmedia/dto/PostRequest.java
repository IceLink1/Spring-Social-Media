package com.example.socialmedia.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PostRequest {
    
    @NotBlank
    @Size(max = 255)
    private String title;
    
    @NotBlank
    private String content;
}