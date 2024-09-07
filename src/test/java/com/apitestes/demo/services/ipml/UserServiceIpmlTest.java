package com.apitestes.demo.services.ipml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.apitestes.demo.dto.UserDto;
import com.apitestes.demo.entity.User;
import com.apitestes.demo.exceptions.exceptionsType.DataIntegratyViolationException;
import com.apitestes.demo.exceptions.exceptionsType.ObjectNotFoundException;
import com.apitestes.demo.repositories.UserRepository;

@SpringBootTest
public class UserServiceIpmlTest {

    private static final String NAME = "lourivan";
    private static final String EMAIL = "lluz@gmail.com";
    private static final String PASSWORD = "123";
    private static final String NEW_PASSWORD = "321";
    private static final String EMAIL_EXISTENT = "jjuz@gmail.com";
    private static final UUID UUID_MOCK_EXISTENT= UUID.randomUUID();
    private static final UUID UUID_MOCK_NOT_EXISTENT= UUID.randomUUID();

    @InjectMocks
    private UserServiceIpml service;

    @Mock
    private ModelMapper mapper;

    @Mock
    private UserRepository userRepository;

    private User user;
    private TypeMap<UserDto, User> mockTypeMap;
    private UserDto userRequestWithNewPassword;
    private User userWithNewPassword;
    private User userWithNewEmail;
    private UserDto userRequestWithSomeOneNewEmail;
    private Optional<User> userWithEmailExistent;
    private UserDto userRequestWithAllAttributes;
    private UserDto userDto;
    private UserDto userDtoUUIDNULL;
    private Optional<User> optionalUser;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        startMock();

    }
    @Test
    void whenFindByIdWithExistUUIDThenReturnAnUserInstance(){
        when(userRepository.findById(UUID_MOCK_EXISTENT)).thenReturn(optionalUser);

        User response = service.findById(UUID_MOCK_EXISTENT);
        validUserAttributes(user,response);

    }
    
    @Test
    void whenEmailExistsAndUserIdIsDifferentThenThrowDataIntegrityViolationException(){
        when(userRepository.findByEmail(anyString())).thenReturn(optionalUser);
        userDto.setId(UUID_MOCK_NOT_EXISTENT);

        try {
            service.validUser(userDto);
        } catch (Exception e) {
            assertEquals(DataIntegratyViolationException.class, e.getClass());
            assertEquals("Email já cadastrado", e.getMessage());
        }
    }
    @Test
    void whenEmailNotFoundButUserIdExistsThenReturnValidUser(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findById(any())).thenReturn(optionalUser);
        User result = service.validUser(userDto);
        validUserAttributes(user, result);
    }
    @Test
    void whenUserEmailExistsButUserIdNotFoundThenThrowObjectNotFoundException(){
        when(userRepository.findByEmail(anyString())).thenReturn(optionalUser);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        try {
            service.validUser(userDto);
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Objeto não encontrado", e.getMessage());
        }
    }

    @Test
    void whenFindbyIdWithNotExistUUIDThenThrowException(){
        when(userRepository.findById(UUID_MOCK_NOT_EXISTENT)).thenThrow(new ObjectNotFoundException("Objeto não encontrado"));

        try {
            service.findById(UUID_MOCK_NOT_EXISTENT);
            
        } catch (Exception e) {
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Objeto não encontrado", e.getMessage());
        }
    }
    
    @Test
    void whenfindAllUsersThenReturnAListOfUsersInstance(){

        when(userRepository.findAll()).thenReturn(List.of(user,user));

        List<User> response = service.findAllUsers();
        User firstUser = response.get(0);
        assertEquals(2, response.size());
        validUserAttributes(user,firstUser);
    }

    @Test
    void whenCreateThenReturnNewUserInstance(){
        when(userRepository.save(any())).thenReturn(user);
        User result = service.create(userDtoUUIDNULL);
        validUserAttributes(user,result);

    }

    @Test
    void whenCreateThenReturnThrowDataIntegratyViolationException(){
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findByEmail(anyString())).thenReturn(optionalUser);
        try{
            User user = service.create(userDtoUUIDNULL);
        }catch(Exception e){
            assertEquals(DataIntegratyViolationException.class, e.getClass());
            assertEquals("Email já cadastrado", e.getMessage());
        }

    }

    @Test
    void whenUpdateWithAllAttributesThenRetrunUserInstanceWithAlteration(){

        when(mapper.getTypeMap(UserDto.class, User.class)).thenReturn(mockTypeMap);
        doAnswer(invocation -> null).when(mockTypeMap).setPropertyCondition(any());

        when(userRepository.findById(UUID_MOCK_EXISTENT)).thenReturn(optionalUser);
        when(userRepository.save(any(User.class))).thenReturn(userWithNewEmail);
        


        User result = service.update(userRequestWithAllAttributes);
        validUserAttributes(userWithNewEmail,result);
    }

    @Test
    void whenUpdateWithSomeoneAttributesThenRetrunUserInstanceWithAlteration(){

        when(mapper.getTypeMap(UserDto.class, User.class)).thenReturn(null);
        when(mapper.createTypeMap(UserDto.class, User.class)).thenReturn(mockTypeMap);
        
        doAnswer(invocation -> null).when(mockTypeMap).setPropertyCondition(any());

        when(userRepository.findById(UUID_MOCK_EXISTENT)).thenReturn(optionalUser);
        when(userRepository.save(any(User.class))).thenReturn(userWithNewEmail);
        


        User result = service.update(userRequestWithSomeOneNewEmail);
        validUserAttributes(userWithNewEmail,result);
    }

    @Test
    void whenUpdateWithNewPasswordThenRetrunUserInstanceWithAlteration(){

        when(mapper.getTypeMap(UserDto.class, User.class)).thenReturn(null);
        when(mapper.createTypeMap(UserDto.class, User.class)).thenReturn(mockTypeMap);
        
        doAnswer(invocation -> null).when(mockTypeMap).setPropertyCondition(any());

        when(userRepository.findById(UUID_MOCK_EXISTENT)).thenReturn(optionalUser);
        when(userRepository.save(any(User.class))).thenReturn(userWithNewPassword);
        


        User result = service.update(userRequestWithNewPassword);
        validUserAttributes(userWithNewPassword,result);
    }


    @Test
    void whenUpdateWithEmailExistentThenThrowDataIntegratyViolationException(){
        when(mapper.getTypeMap(UserDto.class, User.class)).thenReturn(null);
        when(mapper.createTypeMap(UserDto.class, User.class)).thenReturn(mockTypeMap);
        
        doAnswer(invocation -> null).when(mockTypeMap).setPropertyCondition(any());
        when(userRepository.findByEmail(anyString())).thenReturn(userWithEmailExistent);
        

        try {
            User result = service.update(userRequestWithSomeOneNewEmail);
        } catch (Exception e) {
            assertEquals(DataIntegratyViolationException.class, e.getClass());
            assertEquals("Email já cadastrado", e.getMessage());
        }
   
    }


    @Test
    void whenDeleteWithUUIDExistentThenExcludeUser(){
        when(userRepository.findById(UUID_MOCK_EXISTENT)).thenReturn(optionalUser);
        doNothing().when(userRepository).deleteById(UUID_MOCK_EXISTENT);

        service.delete(UUID_MOCK_EXISTENT);

        verify(userRepository,times(1)).deleteById(UUID_MOCK_EXISTENT);
    }

    @Test
    void whenDeleteWithUUIDNonExistentThenThrowException(){
        when(userRepository.findById(UUID_MOCK_EXISTENT)).thenReturn(Optional.empty());

        try{
            service.delete(UUID_MOCK_EXISTENT);
        }catch(Exception e){
            assertEquals(ObjectNotFoundException.class, e.getClass());
            assertEquals("Objeto não encontrado", e.getMessage());
        } 
    }

    void startMock(){
        user = new User(UUID_MOCK_EXISTENT, NAME, EMAIL, PASSWORD);
        userDtoUUIDNULL = new UserDto(null, NAME, EMAIL, PASSWORD);
        userRequestWithNewPassword = new UserDto(UUID_MOCK_EXISTENT, null, null, NEW_PASSWORD);
        userWithNewPassword = new User(UUID_MOCK_EXISTENT, NAME, EMAIL, NEW_PASSWORD);
        userRequestWithSomeOneNewEmail = new UserDto(UUID_MOCK_EXISTENT,null,EMAIL_EXISTENT,null);
        userWithEmailExistent = Optional.of(new User(UUID_MOCK_NOT_EXISTENT,null,EMAIL_EXISTENT,null));
        userRequestWithAllAttributes = new UserDto(UUID_MOCK_EXISTENT,NAME,EMAIL_EXISTENT,PASSWORD);
        userWithNewEmail = new User(UUID_MOCK_EXISTENT,NAME,EMAIL_EXISTENT,PASSWORD);
        userDto = new UserDto(UUID_MOCK_EXISTENT, NAME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new User(UUID_MOCK_EXISTENT, NAME, EMAIL, PASSWORD));
        mockTypeMap = mock(TypeMap.class);
    }

    private void validUserAttributes(User user,User result){
        assertNotNull(result);
        assertEquals(User.class, result.getClass());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), user.getPassword());
    }

}
