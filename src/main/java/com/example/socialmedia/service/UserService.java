package com.example.socialmedia.service;

import com.example.socialmedia.exception.ResourceNotFoundException;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        
        user.setBio(userDetails.getBio());
        user.setProfilePicture(userDetails.getProfilePicture());
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    
    @Transactional
    public void subscribeToUser(Long subscriberId, Long targetId) {
        User subscriber = getUserById(subscriberId);
        User target = getUserById(targetId);
        
        subscriber.getSubscriptions().add(target);
        userRepository.save(subscriber);
    }
    
    @Transactional
    public void unsubscribeFromUser(Long subscriberId, Long targetId) {
        User subscriber = getUserById(subscriberId);
        User target = getUserById(targetId);
        
        subscriber.getSubscriptions().remove(target);
        userRepository.save(subscriber);
    }
    
    public Set<User> getUserSubscriptions(Long userId) {
        User user = getUserById(userId);
        return user.getSubscriptions();
    }
    
    public Set<User> getUserSubscribers(Long userId) {
        User user = getUserById(userId);
        return user.getSubscribers();
    }
}