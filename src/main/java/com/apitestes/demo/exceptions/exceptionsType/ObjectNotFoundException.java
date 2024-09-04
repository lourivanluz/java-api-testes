package com.apitestes.demo.exceptions.exceptionsType;

public class ObjectNotFoundException extends RuntimeException {
    
    public ObjectNotFoundException(String message){
        super(message);
    }
}
