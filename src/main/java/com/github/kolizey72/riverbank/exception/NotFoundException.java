package com.github.kolizey72.riverbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    public NotFoundException() {
        super("Entity not found");
    }

    public NotFoundException(long id) {
        super(String.format("Entity with id %d not found", id));
    }

    public <T> NotFoundException(long id, Class<T> clazz) {
        super(String.format("%s with id %d not found", clazz.getSimpleName(), id));
    }
}
