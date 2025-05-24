package com.example.socialmedia.controller;

import com.example.socialmedia.dto.CommentRequest;
import com.example.socialmedia.dto.MessageResponse;
import com.example.socialmedia.model.Comment;
import com.example.socialmedia.service.CommentService;
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
@RequestMapping("/api/comments")
@Api(tags = "Comments", description = "Comment Management API")
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    @GetMapping
    @ApiOperation("Get All Comments")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }
    
    @GetMapping("/{id}")
    @ApiOperation("Get Comment by ID")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }
    
    @GetMapping("/post/{postId}")
    @ApiOperation("Get Comments by Post")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPost(postId);
    }
    
    @PostMapping("/post/{postId}/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Create Comment")
    public ResponseEntity<Comment> createComment(
            @Valid @RequestBody CommentRequest commentRequest,
            @PathVariable Long postId,
            @PathVariable Long userId) {
        
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        
        Comment createdComment = commentService.createComment(comment, postId, userId);
        return ResponseEntity.ok(createdComment);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Update Comment")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest commentRequest) {
        
        Comment commentDetails = new Comment();
        commentDetails.setContent(commentRequest.getContent());
        
        Comment updatedComment = commentService.updateComment(id, commentDetails);
        return ResponseEntity.ok(updatedComment);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Delete Comment")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(new MessageResponse("Comment deleted successfully"));
    }
    
    @PostMapping("/{commentId}/like/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Like Comment")
    public ResponseEntity<MessageResponse> likeComment(
            @PathVariable Long commentId,
            @PathVariable Long userId) {
        
        boolean liked = commentService.likeComment(commentId, userId);
        String message = liked ? "Comment liked successfully" : "Comment already liked";
        return ResponseEntity.ok(new MessageResponse(message));
    }
    
    @PostMapping("/{commentId}/unlike/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Unlike Comment")
    public ResponseEntity<MessageResponse> unlikeComment(
            @PathVariable Long commentId,
            @PathVariable Long userId) {
        
        boolean unliked = commentService.unlikeComment(commentId, userId);
        String message = unliked ? "Comment unliked successfully" : "Comment was not liked";
        return ResponseEntity.ok(new MessageResponse(message));
    }
}