package com.example.demo.service;

import com.example.demo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<Users> getByPage(String name, String code, Pageable pageable);
    List<Users> getByList();
    Users delete(String id);
    Users save(Users users);
}

