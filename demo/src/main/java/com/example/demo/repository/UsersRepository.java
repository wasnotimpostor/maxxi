package com.example.demo.repository;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, String> {
    @Query(
            value = "select u.*\n"+
                    " from users as u\n"+
                    " where (u.username = binary(?1)" +
                    " or u.email = binary(?1)",
            nativeQuery = true
    )
    Optional<Users> findByUsername(String username);
}
