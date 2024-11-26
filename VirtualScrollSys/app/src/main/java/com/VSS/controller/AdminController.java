package com.VSS.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VSS.model.User;
import com.VSS.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = adminService.addUser(user);
        return ResponseEntity.ok(createdUser);  
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        adminService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();  
    }
     // Ban a user by username
      
    @PostMapping("/ban/{username}")
    public ResponseEntity<String> banUser(@PathVariable String username) {
        int result = adminService.banUserByUsername(username);
        if (result > 0) {
            return new ResponseEntity<>("User banned successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to ban user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Unban a user by username
    @PostMapping("/unban/{username}")
    public ResponseEntity<String> unbanUser(@PathVariable String username) {
        int result = adminService.unbanUserByUsername(username);
        if (result > 0) {
            return new ResponseEntity<>("User unbanned successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to unban user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


