package com.apitestes.demo.domain.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apitestes.demo.domain.User;
import com.apitestes.demo.services.UserService;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userservice;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = userservice.findbyid(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok().body(userservice.findAllUsers());
    }
    

}
