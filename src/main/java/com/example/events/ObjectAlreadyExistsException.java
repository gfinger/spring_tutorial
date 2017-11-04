package com.example.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ObjectAlreadyExistsException extends Exception {
    private static final long serialVersionUID = -5988657553458353455L;
    private Class<?> objectType;
    private String id;

    @Override
    public String getMessage() {
        return String.format("object of type %s with id %s already exists", objectType.getCanonicalName(), id);
    }
}
