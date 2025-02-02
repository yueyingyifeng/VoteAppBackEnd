package com.fy.voteappbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fy.voteappbackend.model.GeneralRequest;
import com.fy.voteappbackend.model.GeneralResponse;
import com.fy.voteappbackend.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @PostMapping("/login")
    public GeneralResponse login(@RequestBody GeneralRequest<User> loginRequest){

        return new GeneralResponse();
    }

    @PostMapping("/register")
    public GeneralResponse register(@RequestBody GeneralRequest<User> loginRequest){

        return new GeneralResponse();
    }
}
