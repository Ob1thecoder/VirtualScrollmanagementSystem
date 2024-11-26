package com.VSS.controller;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.VSS.model.User;
import com.VSS.service.AdminService;



public class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Mock user data
        List<User> mockUsers = Arrays.asList(
                new User("johndoe", "user", "John Doe", "john@example.com", "1234567890", "unique-id"),
                new User("janedoe", "admin", "Jane Doe", "jane@example.com", "0987654321", "unique-id-2")
        );
        when(adminService.getAllUsers()).thenReturn(mockUsers);

        // Perform the GET request
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("johndoe"))
                .andExpect(jsonPath("$[1].username").value("janedoe"));

        // Verify service method call
        verify(adminService, times(1)).getAllUsers();
    }

    @Test
    public void testAddUser() throws Exception {
        // Mock user data
        User user = new User("johndoe", "user", "John Doe", "john@example.com", "1234567890", "unique-id");

        // Perform the POST request
        mockMvc.perform(post("/api/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"fullname\":\"John Doe\",\"username\":\"johndoe\",\"role\":\"user\",\"email\":\"john@example.com\",\"phone\":\"1234567890\",\"IDkey\":\"unique-id\"}"))
                .andExpect(status().isOk());

        // Verify service method call
        verify(adminService, times(1)).addUser(any(User.class));
    }

    
}