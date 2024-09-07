package com.apitestes.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import com.apitestes.demo.dto.UserDto;
import com.apitestes.demo.entity.User;
import com.apitestes.demo.exceptions.exceptionsType.DataIntegratyViolationException;
import com.apitestes.demo.exceptions.exceptionsType.ObjectNotFoundException;
import com.apitestes.demo.services.UserService;


@SpringBootTest
public class UserControllerTest {

    private static final String NAME = "lourivan";
    private static final String EMAIL = "lluz@gmail.com";
    private static final String PASSWORD = "123";
    private static final String NEW_PASSWORD = "321";
    private static final String EMAIL_EXISTENT = "jjuz@gmail.com";
    private static final UUID UUID_MOCK_EXISTENT= UUID.randomUUID();
    private static final UUID UUID_MOCK_NOT_EXISTENT= UUID.randomUUID();
    
    private User user;
    private User user2;
    private UserDto userDtoResponse;
    private UserDto userDto;
    private User nwUser;
    private UserDto nwUserDtoRequestBody;
    private UserDto nwUserDto;


    @InjectMocks
    private UserController resource;

    @Mock
    private ModelMapper mapper;

    @Mock
    private UserService userservice;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        startMoks();
    }


    // @Test
    // void whenIsUUIDwithNonUUIDThrowException(){
    //     try {
    //         resource.isUUID("7");
            
    //     } catch (Exception e) {
    //         assertEquals(DataIntegratyViolationException.class, e.getClass());
    //         assertEquals("UUID não encontrado", e.getMessage());
    //     }
    // }

    // @Test
    // void whenIsUUIDReturnSuccess(){
    //     UUID id = resource.isUUID(String.valueOf(UUID_MOCK_EXISTENT));
    //     assertEquals(UUID.class, id.getClass());
    // }

    @Test
    void whenGetUserByIdReturnSuccess(){

        when(userservice.findById(UUID_MOCK_EXISTENT)).thenReturn(user);
        when(mapper.map(any(), any())).thenReturn(userDtoResponse);

        ResponseEntity<UserDto> response = resource.getUserById(String.valueOf(UUID_MOCK_EXISTENT));

        assertNotNull(response);
        assertNull(response.getBody().getPassword());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

        validUserDTOAttributes(user, response.getBody());

    }

    @Test
    void whenGetUserByIdReturnObjectNotFound(){

        when(userservice.findById(UUID_MOCK_EXISTENT)).thenThrow(new ObjectNotFoundException("Objeto não encontrado"));

        try {
            ResponseEntity<UserDto> response = resource.getUserById(String.valueOf(UUID_MOCK_EXISTENT));
            
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Objeto não encontrado", e.getMessage());

        }
    }

    @Test
    void whenGetUserByIdWithNonUUIDParamThrowExceptionBadRequest(){

        try {
            ResponseEntity<UserDto> response = resource.getUserById("7");
            
        } catch (Exception e) {
            assertEquals(DataIntegratyViolationException.class, e.getClass());
            assertEquals("UUID não encontrado", e.getMessage());
        }
    }

    @Test
    void whenGetAllUserReturnSuccess(){

        when(userservice.findAllUsers()).thenReturn(List.of(user2,user));
        when(mapper.map(any(), any())).thenReturn(userDtoResponse);

        ResponseEntity<List<UserDto>> response = resource.getAllUser();

        assertNotNull(response);
        assertNull(response.getBody().get(0).getPassword());
        assertEquals(response.getBody().size(), 2);
        assertEquals(ResponseEntity.class, response.getClass());

        validUserDTOAttributes(user, response.getBody().get(1));

    }

    @Test
    void whenCreateThenReturnCreatedSuccess(){
        when(userservice.create(any())).thenReturn(user);
        when(mapper.map(any(),any())).thenReturn(userDtoResponse);

        ResponseEntity<UserDto> response = resource.create(userDto);

        assertNotNull(response);
        assertNotNull(response.getHeaders().get("Location"));
        assertNull(response.getBody().getPassword());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

        validUserDTOAttributes(user, response.getBody());



    }
    
    @Test
    void whenUpdateThenReturnSuccess(){

        when(userservice.update(any())).thenReturn(nwUser);
        when(mapper.map(any(), any())).thenReturn(nwUserDto);

        String id = String.valueOf(UUID_MOCK_EXISTENT);

        ResponseEntity<UserDto> response = resource.update(id,nwUserDtoRequestBody);

        assertNotNull(response);
        assertNull(response.getBody().getPassword());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());

        validUserDTOAttributes(nwUser, response.getBody());

    }
    
    @Test
    void whenDeleteThenReturnSuccess(){
        doNothing().when(userservice).delete(UUID_MOCK_EXISTENT);
        String id = String.valueOf(UUID_MOCK_EXISTENT);

        ResponseEntity<UserDto> response = resource.delete(id);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());


    }

    void startMoks(){
        user = new User(UUID_MOCK_EXISTENT, NAME, EMAIL, PASSWORD);
        user2 = new User(UUID_MOCK_NOT_EXISTENT, NAME, EMAIL, PASSWORD);
        userDto = new UserDto(UUID_MOCK_EXISTENT, NAME, EMAIL, PASSWORD);
        userDtoResponse = new UserDto(UUID_MOCK_EXISTENT, NAME, EMAIL, null);

        nwUser = new User(UUID_MOCK_EXISTENT, NAME, "jj", PASSWORD);
        nwUserDtoRequestBody = new UserDto(UUID_MOCK_EXISTENT, null, "jj", null);
        nwUserDto = new UserDto(UUID_MOCK_EXISTENT, NAME, "jj",null);
    }

    private void validUserDTOAttributes(User user,UserDto result){
        assertNotNull(result);
        assertEquals(UserDto.class, result.getClass());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }

}
