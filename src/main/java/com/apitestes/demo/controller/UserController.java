package com.apitestes.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.apitestes.demo.dto.UserDto;
import com.apitestes.demo.entity.User;
import com.apitestes.demo.services.UserService;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userservice;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        User user = userservice.findById(id);
        return ResponseEntity.ok().body(mapper.map(user, UserDto.class));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {

        List<User> list = userservice.findAllUsers();
        List<UserDto> listDto = list.stream().map(user->mapper.map(user, UserDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto user) {
        User userCreate = userservice.create(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(userCreate.getId().toString()).toUri();
        
        return ResponseEntity.created(uri).body(mapper.map(userCreate,UserDto.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable UUID id, @RequestBody UserDto user) {
        user.setId(id);
        User userUpdate = userservice.update(user);
        return ResponseEntity.ok().body(mapper.map(userUpdate,UserDto.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> delete(@PathVariable UUID id){
        userservice.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    

}
