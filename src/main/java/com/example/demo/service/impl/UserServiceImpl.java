package com.example.demo.service.impl;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.UserService;
import org.json.JSONObject;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    Users usersJson(String id, JSONObject jsonObject){
        String name = jsonObject.optString("name");
        String email = jsonObject.optString("email");
        String address = jsonObject.optString("address");
        String ktp_number = jsonObject.optString("ket_number");
        String phone_number = jsonObject.optString("phone_number");

        Date date= new Date();
        Timestamp ts = new Timestamp(date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        format.format(ts);

        Users users;
        if (id == null){
            return null;
        } else {
            Optional<Users> exist = usersRepository.findById(id);
            if (exist.isPresent())
                users = new Users(id, name, exist.get().getUsername(), email, exist.get().getPassword(),
                        exist.get().getCode(), address, ktp_number, phone_number, exist.get().getCreated_at(), ts, exist.get().getRoles());
            else
                return null;
        }
        return users;
    }

    @Override
    public Users save(String id, String request) {
        JSONObject jsonObject = new JSONObject(request);
        Optional<Users> exist = usersRepository.findById(id);
        Users users = usersJson(id, jsonObject);
        try {
            if (!exist.isPresent())
                throw new Exception("Data Not Found!");
            if (users == null){
                throw new Exception("Failed Update Data");
            } else {
                Users save = usersRepository.save(users);
            }
        } catch (Exception e){
            logger.error("message {} ",e);
        }
        return users;
    }
}

