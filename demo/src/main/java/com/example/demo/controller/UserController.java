package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.utils.GlobalApiResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "users", produces = MediaType.APPLICATION_JSON_VALUE)

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "page", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalApiResponse<?> getBypage(@RequestBody String body){
        JSONObject jsonObject = new JSONObject(body);
        String name = jsonObject.optString("name");
        String code = jsonObject.optString("code");
        Integer page = jsonObject.optInt("page", 0);
        if(page > 0) page--;
        Integer per_page = jsonObject.optInt("per_page", 10);
        Pageable paging = PageRequest.of(page, per_page);
        return new GlobalApiResponse<>(
                HttpStatus.OK.value(),
                this.userService.getByPage(name, code, paging)
        );
    }

    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalApiResponse<?> getByList(){
        return new GlobalApiResponse<>(
                HttpStatus.OK.value(),
                this.userService.getByList()
        );
    }

    @PostMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public GlobalApiResponse<?> delete(@RequestParam(name = "id") String id){
        return new GlobalApiResponse<>(
                HttpStatus.OK.value(),
                this.userService.delete(id)
        );
    }
}

