package com.VSS.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.VSS.model.User;
import com.VSS.util.UserRespository;

public class AdminServiceTest {
    @Mock
    private UserRespository userRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // Mock user data
        List<User> mockUsers = Arrays.asList(
                new User("johndoe", "user", "John Doe", "john@example.com", "1234567890", "unique-id"),
                new User("janedoe", "admin", "Jane Doe", "jane@example.com", "0987654321", "unique-id-2"));
        when(userRepository.findAllUsers()).thenReturn(mockUsers);

        // Call the service method
        List<User> users = adminService.getAllUsers();

        // Assertions
        assertEquals(2, users.size(), "Expected two users");
        assertEquals("johndoe", users.get(0).getUsername(), "First user's username should be 'johndoe'");
        verify(userRepository, times(1)).findAllUsers();
    }

    @Test
    public void testAddUser() {
        User user = new User("johndoe", "user", "John Doe", "john@example.com", "1234567890", "unique-id");

        adminService.addUser(user);

        verify(userRepository, times(1)).saveUser(user);
    }

    @Test
    public void testDeleteUserByUsername() {
        // Mock a username
        String username = "johndoe";

        // Call the service method
        adminService.deleteUserByUsername(username);

        // Verify repository interaction
        verify(userRepository, times(1)).deleteUserByUsername(username);
    }

}