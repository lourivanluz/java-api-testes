package com.apitestes.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apitestes.demo.entity.User;
import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User,UUID>{
    Optional<User> findByEmail(String email);
}
