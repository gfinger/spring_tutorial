package com.example.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ObjectNotFoundException extends Exception {
    private static final long serialVersionUID = 1080113026801028312L;
    private Class<?> objectType;
    private String id;
    
    @Override
    public String getMessage() {
        return String.format("object of type %s with id %s not found", objectType.getCanonicalName(), id);
    }
}
