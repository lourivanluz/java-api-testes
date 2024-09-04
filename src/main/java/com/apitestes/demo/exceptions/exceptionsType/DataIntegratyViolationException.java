package com.apitestes.demo.exceptions.exceptionsType;

public class DataIntegratyViolationException extends RuntimeException {
    
    public DataIntegratyViolationException(String message){
        super(message);
    }
}