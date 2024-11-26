package com.VSS.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.VSS.model.User;
import com.VSS.util.UserRespository;


@Service
public class AdminService {

    private final UserRespository userRepository;

    public AdminService(UserRespository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    // Add a new user
    public User addUser(User user) {
        userRepository.saveUser(user);
        return user;  
    }

    // Delete a user by username
    public void deleteUserByUsername(String username) {
        userRepository.deleteUserByUsername(username);
    }

    

    // Ban a user by username
    public int banUserByUsername(String username) {
        return userRepository.banUserByUsername(username);
    }
    // Unban a user by username
    public int unbanUserByUsername(String username) {
        return userRepository.unbanUserByUsername(username);
    }
}
