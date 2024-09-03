package com.apitestes.demo.services;

import java.util.List;
import java.util.UUID;

import com.apitestes.demo.domain.User;

public interface UserService {
   User findbyid(UUID id);
   List<User> findAllUsers();
}
