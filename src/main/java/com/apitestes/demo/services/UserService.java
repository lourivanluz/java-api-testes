package com.apitestes.demo.services;

import java.util.List;
import java.util.UUID;

import com.apitestes.demo.dto.UserDto;
import com.apitestes.demo.entity.User;

public interface UserService {
   User findbyid(UUID id);
   List<User> findAllUsers();
   User create(UserDto user);
   User update(UserDto user);
   void delete(UUID id);
}
