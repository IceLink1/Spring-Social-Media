package com.example.socialmedia.controller;

import com.example.socialmedia.dto.MessageResponse;
import com.example.socialmedia.model.User;
import com.example.socialmedia.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Api(tags = "Users", description = "User Management API")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Get All Users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Get User by ID")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Get User by Username")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Update User")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Delete User")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }
    
    @PostMapping("/{subscriberId}/subscribe/{targetId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Subscribe to User")
    public ResponseEntity<MessageResponse> subscribeToUser(
            @PathVariable Long subscriberId,
            @PathVariable Long targetId) {
        userService.subscribeToUser(subscriberId, targetId);
        return ResponseEntity.ok(new MessageResponse("Subscribed successfully"));
    }
    
    @PostMapping("/{subscriberId}/unsubscribe/{targetId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Unsubscribe from User")
    public ResponseEntity<MessageResponse> unsubscribeFromUser(
            @PathVariable Long subscriberId,
            @PathVariable Long targetId) {
        userService.unsubscribeFromUser(subscriberId, targetId);
        return ResponseEntity.ok(new MessageResponse("Unsubscribed successfully"));
    }
    
    @GetMapping("/{userId}/subscriptions")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Get User Subscriptions")
    public ResponseEntity<Set<User>> getUserSubscriptions(@PathVariable Long userId) {
        Set<User> subscriptions = userService.getUserSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }
    
    @GetMapping("/{userId}/subscribers")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @ApiOperation("Get User Subscribers")
    public ResponseEntity<Set<User>> getUserSubscribers(@PathVariable Long userId) {
        Set<User> subscribers = userService.getUserSubscribers(userId);
        return ResponseEntity.ok(subscribers);
    }
}