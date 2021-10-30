package com.example.demo.service.impl;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @PersistenceContext
    private EntityManager em;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Page<Users> getByPage(String name, String code, Pageable pageable) {
        String q = "select u.* from users as u\n";
        String c = "select count(u.id) from users as u\n";
        StringBuilder stringBuilder = new StringBuilder();
        if (name != null){
            if (stringBuilder.length() > 0) stringBuilder.append(" and ");
            else stringBuilder.append(" where ");
            stringBuilder.append(" coalesce(u.name, '') like '%"+name+"%'");
        }
        if (code != null){
            if (stringBuilder.length() > 0) stringBuilder.append(" and ");
            else stringBuilder.append(" where ");
            stringBuilder.append(" coalesce(u.code, '') like '%"+code+"%'");
        }
        String count = em.createNativeQuery(c + stringBuilder).getSingleResult().toString();
        Integer counts = Integer.parseInt(count);
        Query query = em.createNativeQuery(q + stringBuilder, Users.class);
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        List<Users> list = query.getResultList();
        Page<Users> page = new PageImpl<>(list, pageable, counts);
        return page;
    }

    @Override
    public List<Users> getByList() {
        List<Users> list = new ArrayList<>();
        try {
            list = usersRepository.findAll();
        } catch (Exception e){
            logger.error("message {} ", e);
        }
        return list;
    }

    @Override
    public Users delete(String id) {
        Users exist = usersRepository.findById(id).orElse(null);
        try {
            if (exist == null){
                throw new Exception("User not found!");
            } else {
                usersRepository.deleteById(id);
            }
        } catch (Exception e){
            logger.error("message {} ", e);
        }
        return exist;
    }

    @Override
    public Users save(Users users) {
        Optional<Users> exist = usersRepository.findById(users.getId());

        try {
            if (!exist.isPresent())
                throw new Exception("Data Not Found!");
            if (users == null){
                throw new Exception("Failed Update Data");
            } else {
                Users save = Users.builder()
                        .id(exist.get().getId())
                        .name(users.getName())
                        .username(exist.get().getUsername())
                        .email(users.getEmail())
                        .password(new BCryptPasswordEncoder().encode(users.getPassword()))
                        .code(exist.get().getCode())
                        .address(users.getAddress())
                        .ktp_number(users.getKtp_number())
                        .phone_number(users.getPhone_number())
                        .roles(exist.get().getRoles())
                        .build();
                usersRepository.save(save);
            }
        } catch (Exception e){
            logger.error("message {} ",e);
        }
        return users;
    }
}

