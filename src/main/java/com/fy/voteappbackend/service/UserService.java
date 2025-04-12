package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.User;

import java.io.UnsupportedEncodingException;

public interface UserService {
    //返回的 token
    String login(User user);

    //返回的 token
    String register(User user) throws UnsupportedEncodingException;

    String updatePassword(long uid,String oldPassword,String newPassword);

}
