package com.github.kolizey72.riverbank.exception;

public class ListOverflowException extends RuntimeException{

    public ListOverflowException() {
    }

    public ListOverflowException(String message) {
        super(message);
    }
}
