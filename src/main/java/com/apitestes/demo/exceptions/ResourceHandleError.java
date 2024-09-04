package com.apitestes.demo.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apitestes.demo.exceptions.exceptionsType.DataIntegratyViolationException;
import com.apitestes.demo.exceptions.exceptionsType.ObjectNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceHandleError {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardExpection> objectNotFound(ObjectNotFoundException ex, HttpServletRequest request){
        StandardExpection error = 
        new StandardExpection(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegratyViolationException.class)
    public ResponseEntity<StandardExpection> dataIntegratyViolationException(DataIntegratyViolationException ex, HttpServletRequest request){
        StandardExpection error = 
        new StandardExpection(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
