package ru.otus.exception;

public class NotFoundBeanException extends RuntimeException {
    public NotFoundBeanException(String message) {
        super(message);
    }
}
