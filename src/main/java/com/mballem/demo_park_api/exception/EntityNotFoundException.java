package com.mballem.demo_park_api.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private String recurso;
    private String codigo;
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String codigo, String recurso) {
        this.codigo = codigo;
        this.recurso = recurso;
    }
}
