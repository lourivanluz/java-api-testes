package com.apitestes.demo.services.ipml;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apitestes.demo.domain.User;
import com.apitestes.demo.domain.repositories.UserRepository;
import com.apitestes.demo.services.UserService;

@Service
public class UserServiceIpml implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findbyid(UUID id) {
        Optional<User> user =  userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        List<User> list = userRepository.findAll();
        return list;
    }
    
}
