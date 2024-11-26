package com.VSS.util;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.VSS.model.User;

public class UserRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRespository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for findAllUsers() method
    @Test
    public void testFindAllUsers() {
        // Mock data from the database
        User user1 = new User("username1", "Admin", "John Doe", "john@example.com", "123456789", "ID123");
        User user2 = new User("username2", "User", "Jane Doe", "jane@example.com", "987654321", "ID456");

        List<User> expectedUsers = Arrays.asList(user1, user2);

        // Mock the jdbcTemplate.query() method
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(expectedUsers);

        // Call the method
        List<User> actualUsers = userRepository.findAllUsers();

        // Verify the results
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        assertEquals("John Doe", actualUsers.get(0).getFullname());
        assertEquals("Jane Doe", actualUsers.get(1).getFullname());

        // Verify that jdbcTemplate.query() was called once with the correct SQL
        // statement
        verify(jdbcTemplate, times(1)).query(eq("SELECT * FROM authapp_customuser"), any(RowMapper.class));
    }

    // Test case for saveUser() method
    @Test
    public void testSaveUser() {
        // Mock User object
        User user = new User("username1", "Admin", "John Doe", "john@example.com", "123456789", "ID123");

        // Mock the jdbcTemplate.update() method
        when(jdbcTemplate.update(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString())).thenReturn(1);

        // Call the saveUser method
        int result = userRepository.saveUser(user);

        // Verify that the result is 1 (indicating success)
        assertEquals(1, result);

        // Verify that jdbcTemplate.update() was called with the correct SQL and
        // parameters
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO authapp_customuser (fullname, username, role, email, phoneNumber, IDkey, password) VALUES (?, ?, ?, ?, ?, ?, ?)"),
                eq(user.getFullname()),
                eq(user.getUsername()),
                eq(user.getRole()),
                eq(user.getEmail()),
                eq(user.getPhone()),
                eq(user.getIDkey()),
                anyString() 
        );
    }

    // Test case for deleteUserByUsername() method
    @Test
    public void testDeleteUserByUsername() {
        // Mock the jdbcTemplate.update() method
        when(jdbcTemplate.update(anyString(), anyString())).thenReturn(1);

        // Call the deleteUserByUsername method
        int result = userRepository.deleteUserByUsername("username1");

        // Verify that the result is 1 (indicating success)
        assertEquals(1, result);

        // Verify that jdbcTemplate.update() was called with the correct SQL and
        // username parameter
        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM authapp_customuser WHERE username = ?"), eq("username1"));
    }

}