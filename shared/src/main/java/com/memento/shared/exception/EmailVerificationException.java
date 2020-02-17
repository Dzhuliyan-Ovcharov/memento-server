package com.memento.shared.exception;

public class EmailVerificationException extends RuntimeException {

    private static final long serialVersionUID = 9016765657097092361L;

    public EmailVerificationException() {
        super();
    }

    public EmailVerificationException(String message) {
        super(message);
    }

    public EmailVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailVerificationException(Throwable cause) {
        super(cause);
    }

    protected EmailVerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
