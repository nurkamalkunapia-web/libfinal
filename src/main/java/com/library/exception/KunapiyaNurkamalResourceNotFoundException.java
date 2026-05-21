package com.library.exception;

public class KunapiyaNurkamalResourceNotFoundException extends RuntimeException {

    public KunapiyaNurkamalResourceNotFoundException(String message) {
        super(message);
    }

    public KunapiyaNurkamalResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}