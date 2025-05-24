package com.example.socialmedia.service;

import com.example.socialmedia.exception.ResourceNotFoundException;
import com.example.socialmedia.model.Comment;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.CommentRepository;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
    
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }
    
    public List<Comment> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }
    
    @Transactional
    public Comment createComment(Comment comment, Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        comment.setPost(post);
        comment.setUser(user);
        return commentRepository.save(comment);
    }
    
    @Transactional
    public Comment updateComment(Long id, Comment commentDetails) {
        Comment comment = getCommentById(id);
        
        comment.setContent(commentDetails.getContent());
        
        return commentRepository.save(comment);
    }
    
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = getCommentById(id);
        commentRepository.delete(comment);
    }
    
    @Transactional
    public boolean likeComment(Long commentId, Long userId) {
        Comment comment = getCommentById(commentId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        boolean added = comment.addLike(user);
        commentRepository.save(comment);
        return added;
    }
    
    @Transactional
    public boolean unlikeComment(Long commentId, Long userId) {
        Comment comment = getCommentById(commentId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        boolean removed = comment.removeLike(user);
        commentRepository.save(comment);
        return removed;
    }
}