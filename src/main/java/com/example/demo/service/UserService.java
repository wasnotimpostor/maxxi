package com.example.demo.service;

import com.example.demo.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Page<Users> getByPage(String name, String code, Pageable pageable);
    List<Users> getByList();
    Users delete(String id);
    ResponseEntity<Map<String, Object>> save(String id, String request);
    ResponseEntity<Map<String, Object>> findById(String id);
}

