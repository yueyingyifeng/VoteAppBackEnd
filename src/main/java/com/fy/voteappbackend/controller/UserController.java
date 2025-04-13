package com.fy.voteappbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.fy.voteappbackend.Tools.PictureTools;
import com.fy.voteappbackend.Tools.SnowflakeUtil;
import com.fy.voteappbackend.context.UserContext;
import com.fy.voteappbackend.model.GeneralRequest;
import com.fy.voteappbackend.model.GeneralResponse;
import com.fy.voteappbackend.model.User;
import com.fy.voteappbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;

import static com.fy.voteappbackend.Tools.PictureTools.findFileByNameWithoutExtension;

@Slf4j
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

        //  雪花算法生成uid
        user.setUid(SnowflakeUtil.snowflakeId());

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

    @PostMapping("/changePsw")
    public GeneralResponse ChangePsw(@RequestParam String oldPassword,@RequestParam String newPassword){

        //获取用户uid
        Long uid = UserContext.getCurrentId();
        if (uid == null) {
            return new GeneralResponse().makeResponse("err","获取用户id失败");
        }

        String massage = userService.updatePassword(uid,oldPassword,newPassword);

        GeneralResponse response = new GeneralResponse();
        JSONObject data = new JSONObject();
        data.put("data", massage);
        return response.makeResponse("ok", "none").addData(data);
    }

    @PostMapping("/upload_avatar")
    public GeneralResponse uploadAvatar(MultipartFile img) throws IOException {

        //获取用户uid
        Long uid = UserContext.getCurrentId();
        if (uid == null) {
            return new GeneralResponse().makeResponse("err","获取用户id失败");
        }

        //获取该用户指定存储头像文件地址
        Optional<Path> foundFile = findFileByNameWithoutExtension(String.valueOf(uid));


        //如果用户已经上传文件则删除之前文件
        foundFile.ifPresentOrElse(
                path -> {
                    System.out.println("找到文件: " + path.toAbsolutePath());
                    //删除已有图片
                    foundFile.ifPresent(value -> {if(value.toFile().delete()){
                        log.info("照片文件删除成功");
                    }else {
                        log.info("照片文件删除失败");
                    }
                    });

                    //存储照片
                    try {
                        PictureTools.savePicture(img,String.valueOf(uid));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    log.info("未找到匹配文件");
                    //存储照片
                    try {
                        PictureTools.savePicture(img,String.valueOf(uid));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }}
        );

        GeneralResponse response = new GeneralResponse();
        JSONObject data = new JSONObject();
        data.put("data", "success");
        return response.makeResponse("ok", "none").addData(data);
    }

    @PostMapping("/get_avatar")
    public GeneralResponse getAvatar() throws IOException {

        //获取用户uid
        Long uid = UserContext.getCurrentId();
        if (uid == null) {
            return new GeneralResponse().makeResponse("err","获取用户id失败");
        }

        //获取该用户指定存储头像文件地址
        Optional<Path> foundFile = findFileByNameWithoutExtension(String.valueOf(uid));

        JSONObject data = new JSONObject();

        //
        foundFile.ifPresentOrElse(path -> data.put("data", PictureTools.imgUrlCreate(foundFile.get().toFile())),
                () -> data.put("data","none picture"));

        GeneralResponse response = new GeneralResponse();

        return response.makeResponse("ok", "none").addData(data);
    }
}
