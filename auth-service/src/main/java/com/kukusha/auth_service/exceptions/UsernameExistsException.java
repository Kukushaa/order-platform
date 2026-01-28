package com.kukusha.auth_service.exceptions;

public class UsernameExistsException extends Exception {
    public UsernameExistsException() {
    }

    public UsernameExistsException(String message) {
        super(message);
    }

    public UsernameExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameExistsException(Throwable cause) {
        super(cause);
    }

    public UsernameExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
