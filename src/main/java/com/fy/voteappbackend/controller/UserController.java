package com.fy.voteappbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fy.voteappbackend.context.UserContext;
import com.fy.voteappbackend.model.GeneralRequest;
import com.fy.voteappbackend.model.GeneralResponse;
import com.fy.voteappbackend.model.User;
import com.fy.voteappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public GeneralResponse login(@RequestBody GeneralRequest<User> request){
        User user = request.getData();
        GeneralResponse response = new GeneralResponse();



        if(user == null)
            return response.makeResponse("err", "请求错误");

        String token = userService.login(user);
        if(token == null)
            return  response.makeResponse("err token", "请求错误");

        if(token.equals("none"))
            return response.makeResponse("login failed, no such user", "用户密码或账号错误");

        JSONObject data = new JSONObject();
        data.put("token", token);
        return response.makeResponse("ok", "none").addData(data);
    }

    @PostMapping("/register")
    public GeneralResponse register(@RequestBody GeneralRequest<User> request) throws UnsupportedEncodingException {
        User user = request.getData();
        System.out.println(user);
        GeneralResponse response = new GeneralResponse();
        if(user == null)
            return response.makeResponse("err", "请求错误");


        user.setUid(new Random().nextInt(100));// 应该由微信小程序提供


        String token = userService.register(user);
        if(token == null)
            return response.makeResponse("err token", "创建用户失败");
            
        if(token.equals("phone_exists"))
            return response.makeResponse("register failed", "该手机号已被注册");


        JSONObject data = new JSONObject();
        data.put("token", token);
        return response.makeResponse("ok", "none").addData(data);
    }

    @GetMapping("/changePsw")
    public GeneralResponse ChangePsw(@RequestParam String password) throws UnsupportedEncodingException {

        //获取用户uid
        Long uid = UserContext.getCurrentId();
        if (uid == null) {
            return new GeneralResponse().makeResponse("err","获取用户id失败");
        }

        String massage = userService.updatePassword(uid,password);

        GeneralResponse response = new GeneralResponse();
        JSONObject data = new JSONObject();
        data.put("token", massage);
        return response.makeResponse("ok", "none").addData(data);
    }


}
