package com.apitestes.demo.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.apitestes.demo.exceptions.exceptionsType.DataIntegratyViolationException;
import com.apitestes.demo.exceptions.exceptionsType.ObjectNotFoundException;

@SpringBootTest
public class ResourceHandleErrorTest {
    
    @InjectMocks
    private ResourceHandleError handleError;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

    }
    
    @Test
    void whenObjectNotFoundThenReturnResponseEntity(){
        ResponseEntity<StandardExpection> response = handleError.objectNotFound(
            new ObjectNotFoundException("Objeto não encontrado"), new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(StandardExpection.class, response.getBody().getClass());
        assertEquals("Objeto não encontrado", response.getBody().getError());
    }
    
    @Test
    void whenDataIntegratyViolationExceptionThenReturnResponseEntity (){
        ResponseEntity<StandardExpection> response = handleError.dataIntegratyViolationException(
            new DataIntegratyViolationException("Email já cadastrado"), new MockHttpServletRequest());

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(StandardExpection.class, response.getBody().getClass());
        assertEquals("Email já cadastrado", response.getBody().getError());

    }

}
