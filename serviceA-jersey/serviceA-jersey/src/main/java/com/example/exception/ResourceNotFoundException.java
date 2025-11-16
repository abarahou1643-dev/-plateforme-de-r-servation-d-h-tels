package com.example.exception;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s with id %d not found", resourceName, id));
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
