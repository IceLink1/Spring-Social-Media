package com.example.socialmedia.controller;

import com.example.socialmedia.dto.MessageResponse;
import com.example.socialmedia.dto.PostRequest;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
@Api(tags = "Posts", description = "Post Management API")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @GetMapping
    @ApiOperation("Get All Posts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Get Post by ID")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        postService.incrementViewCount(id);
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/user/{userId}")
    @ApiOperation("Get Posts by User")
    public List<Post> getPostsByUser(@PathVariable Long userId) {
        return postService.getPostsByUser(userId);
    }
    
    @GetMapping("/feed/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Get Feed Posts")
    public List<Post> getFeedPosts(@PathVariable Long userId) {
        return postService.getFeedPosts(userId);
    }
    
    @PostMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Create Post")
    public ResponseEntity<Post> createPost(
            @Valid @RequestBody PostRequest postRequest,
            @PathVariable Long userId) {
        
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        
        Post createdPost = postService.createPost(post, userId);
        return ResponseEntity.ok(createdPost);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Update Post")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest postRequest) {
        
        Post postDetails = new Post();
        postDetails.setTitle(postRequest.getTitle());
        postDetails.setContent(postRequest.getContent());
        
        Post updatedPost = postService.updatePost(id, postDetails);
        return ResponseEntity.ok(updatedPost);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Delete Post")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(new MessageResponse("Post deleted successfully"));
    }
    
    @PostMapping("/{postId}/like/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Like Post")
    public ResponseEntity<MessageResponse> likePost(
            @PathVariable Long postId,
            @PathVariable Long userId) {
        
        boolean liked = postService.likePost(postId, userId);
        String message = liked ? "Post liked successfully" : "Post already liked";
        return ResponseEntity.ok(new MessageResponse(message));
    }
    
    @PostMapping("/{postId}/unlike/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Unlike Post")
    public ResponseEntity<MessageResponse> unlikePost(
            @PathVariable Long postId,
            @PathVariable Long userId) {
        
        boolean unliked = postService.unlikePost(postId, userId);
        String message = unliked ? "Post unliked successfully" : "Post was not liked";
        return ResponseEntity.ok(new MessageResponse(message));
    }
}