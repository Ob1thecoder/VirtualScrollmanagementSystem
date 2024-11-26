package com.VSS.model;

import java.security.SecureRandom;

public class User {

    private Long id;
    private String fullname;
    private String username;
    private String role;
    private String email;
    private String phoneNumber;
    private String IDkey;
    private boolean banned;

    public User() {
    }

    public User(String username, String role, String fullname, String email, String phone, String IDkey) {
        this.username = username;
        this.fullname = fullname;
        this.role = role;
        this.email = email;
        this.phoneNumber = phone;
        this.IDkey = IDkey;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phoneNumber;
    }

    public void setPhone(String phone) {
        this.phoneNumber = phone;
    }

    public String getIDkey() {
        return IDkey;
    }

    public void setIDkey(String IDkey) {
        this.IDkey = IDkey;
    }

    public String generateRandomPassword(int length) {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(index));
        }

        return password.toString();
    }
    public void setBanned(boolean is_banned) {
        this.banned= is_banned;
    }

    public String getPassword() {
        return generateRandomPassword(10);
    }

    public void setPassword(String password) {
        
    }

}
