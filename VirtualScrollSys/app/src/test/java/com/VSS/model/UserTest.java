package com.VSS.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize a user object before each test
        user = new User("john_doe", "Admin", "John Doe", "john.doe@example.com", "1234567890", "ID123");
    }

    @Test
    public void testConstructor() {
        // Check if constructor initializes correctly
        assertEquals("john_doe", user.getUsername());
        assertEquals("Admin", user.getRole());
        assertEquals("John Doe", user.getFullname());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhone());
        assertEquals("ID123", user.getIDkey());
    }

    @Test
    public void testSetAndGetId() {
        Long id = 123L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testSetAndGetFullname() {
        String fullname = "Jane Doe";
        user.setFullname(fullname);
        assertEquals(fullname, user.getFullname());
    }

    @Test
    public void testSetAndGetUsername() {
        String username = "jane_doe";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testSetAndGetRole() {
        String role = "User";
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    public void testSetAndGetEmail() {
        String email = "jane.doe@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetAndGetPhone() {
        String phone = "0987654321";
        user.setPhone(phone);
        assertEquals(phone, user.getPhone());
    }

    @Test
    public void testSetAndGetIDkey() {
        String IDkey = "ID456";
        user.setIDkey(IDkey);
        assertEquals(IDkey, user.getIDkey());
    }

    @Test
    public void testGenerateRandomPassword_Length() {
        // Test if the generated password has the correct length
        int length = 12;
        String password = user.generateRandomPassword(length);
        assertNotNull(password);
        assertEquals(length, password.length());
    }

    @Test
    public void testGenerateRandomPassword_Content() {
        // Test if the generated password contains allowed characters
        int length = 10;
        String password = user.generateRandomPassword(length);
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        
        for (char c : password.toCharArray()) {
            assertTrue(allowedChars.indexOf(c) >= 0);
        }
    }

    @Test
    public void testGenerateRandomPassword_Uniqueness() {
        // Test if passwords generated multiple times are different
        String password1 = user.generateRandomPassword(10);
        String password2 = user.generateRandomPassword(10);
        assertNotEquals(password1, password2);
    }
}