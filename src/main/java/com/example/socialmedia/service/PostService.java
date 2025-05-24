package com.example.socialmedia.service;

import com.example.socialmedia.exception.ResourceNotFoundException;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }
    
    public List<Post> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<Post> getFeedPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        List<User> subscriptions = new ArrayList<>(user.getSubscriptions());
        subscriptions.add(user); // Include user's own posts
        
        return postRepository.findPostsByUsersOrderByCreatedAtDesc(subscriptions);
    }
    
    @Transactional
    public Post createPost(Post post, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        post.setUser(user);
        return postRepository.save(post);
    }
    
    @Transactional
    public Post updatePost(Long id, Post postDetails) {
        Post post = getPostById(id);
        
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        
        return postRepository.save(post);
    }
    
    @Transactional
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
    
    @Transactional
    public void incrementViewCount(Long id) {
        Post post = getPostById(id);
        post.incrementViewCount();
        postRepository.save(post);
    }
    
    @Transactional
    public boolean likePost(Long postId, Long userId) {
        Post post = getPostById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        boolean added = post.addLike(user);
        postRepository.save(post);
        return added;
    }
    
    @Transactional
    public boolean unlikePost(Long postId, Long userId) {
        Post post = getPostById(postId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        boolean removed = post.removeLike(user);
        postRepository.save(post);
        return removed;
    }
}