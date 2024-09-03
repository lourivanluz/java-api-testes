package com.apitestes.demo.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apitestes.demo.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User,UUID>{
    
}
