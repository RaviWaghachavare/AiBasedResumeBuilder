
package com.switchproject.demo.service;

public class PendingRegistration {

    private final String username;
    private final String email;
    private final String encodedPassword;

    public PendingRegistration(
            String username,
            String email,
            String encodedPassword) {

        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }
}
