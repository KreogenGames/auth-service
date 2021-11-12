package com.clone.instagram.authservice.exception;

public class UsernameIsNotExistsException extends RuntimeException {
    public UsernameIsNotExistsException(String message) {
        super(message);
    }
}
