package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.User;

public interface UserService {
    //返回的 token
    String login(User user);

    //返回的 token
    String register(User user);
}
