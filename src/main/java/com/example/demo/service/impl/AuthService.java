package com.example.demo.service.impl;

import com.example.demo.config.CustomException;
import com.example.demo.config.security.jwt.JwtProvider;
import com.example.demo.config.security.request.Login;
import com.example.demo.config.security.request.Register;
import com.example.demo.config.security.response.JwtResponse;
import com.example.demo.entity.RoleName;
import com.example.demo.entity.Roles;
import com.example.demo.entity.Users;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    private String generateCode(String uniqueCode, String username){
        String u = "";
        String[] split = username.split(" ");
        String uu = split[0].substring(0, 2);
        Integer length = split.length;
        if (length > 1) {
            u = split[1].substring(0, 1);
        }
        String code = uniqueCode + uu.toUpperCase() + u;
        Optional<Users> exist = usersRepository.checkCode(code);
        if (exist.isPresent()){
            String currCode = exist.get().getCode();
            Integer c = Integer.parseInt(currCode.substring(currCode.length() - 5));
            c = c+1;
            if (c < 10){
                code = "00"+c;
            }
        } else {
            code = code+"000";
        }
        return code;
    }

    public Users save(Register register, Set<Roles> roles){
        Users users = Users.builder()
                .name(register.getName())
                .username(register.getUsername())
                .email(register.getEmail())
                .password(new BCryptPasswordEncoder().encode(register.getPassword()))
                .code(generateCode("PTN", register.getUsername()))
                .address(register.getAddress())
                .ktp_number(register.getKtp_number())
                .phone_number(register.getPhone_number())
                .roles(roles)
                .build();
        return usersRepository.save(users);
    }

    public Users register(Register register){
        Set<Integer> strRoles = register.getRole();
        Set<Roles> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case 1:
                    Roles admin = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() ->
                            new RuntimeException("Role Not Found"));
                    roles.add(admin);
                    break;
                }
            });
        return this.save(register,roles);
    }

    public ResponseEntity<Map<String, Object>> login(Login login){
        Map<String, Object> rsp;
        try {
            Optional<Users> exist = usersRepository.findByUsername(login.getUsernameOrEmail());
            if (!exist.isPresent()){
                rsp = CustomException.errorMsg(404, "Invalid username/password", "Process Failed");
                return new ResponseEntity<>(rsp, HttpStatus.OK);
            }
            String currPass = exist.get().getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean passMatch = passwordEncoder.matches(login.getPassword(), currPass);
            if (!passMatch){
                rsp = CustomException.errorMsg(404, "Invalid username/password", "Process Failed");
            } else {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(login.getUsernameOrEmail(), login.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtProvider.generateToken(exist.get(), 0);
                rsp = CustomException.succRsp("true", "Login in success", new JwtResponse(jwt));
            }
        } catch (AuthenticationException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(rsp, HttpStatus.OK);
    }

    public Object myInfo(Authentication authentication){
        return authentication.getPrincipal();
    }
}
