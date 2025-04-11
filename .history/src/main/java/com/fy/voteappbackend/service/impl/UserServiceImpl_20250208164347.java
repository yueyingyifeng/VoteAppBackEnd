package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fy.voteappbackend.Tools.JWTUtils;
import com.fy.voteappbackend.mapper.UserAuthMapper;
import com.fy.voteappbackend.mapper.UserInfoMapper;
import com.fy.voteappbackend.model.User;
import com.fy.voteappbackend.model.UserAuth;
import com.fy.voteappbackend.model.UserInfo;
import com.fy.voteappbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserInfoMapper userInfoMapper;
    private final UserAuthMapper userAuthMapper;

    @Override
    public String login(User user) {
        if (user == null || user.getPhone() == null || user.getPsw() == null)
            return null;


        LambdaQueryWrapper<UserInfo> infoWrapper = new LambdaQueryWrapper<>();
        infoWrapper.eq(UserInfo::getPhone, user.getPhone());
        UserInfo userInfo = userInfoMapper.selectOne(infoWrapper);

        if (userInfo == null) {
            return "none";
        }

        UserAuth userAuth = userAuthMapper.selectById(userInfo.getUid());


        if (userAuth == null || !userAuth.getPsw().equals(user.getPsw())) {
            return "none";
        }

        try {
            return generateToken(new User(){{
                setUid(userInfo.getUid());
                setPhone(user.getPhone());
            }});
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String register(User user) throws UnsupportedEncodingException {
        if (user == null || user.getPhone() == null || user.getPsw() == null)
            return null;

        // 检查手机号是否注册
        String phone = user.getPhone();
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getPhone, phone);
        if (userInfoMapper.selectCount(queryWrapper) > 0) {
            return "none";
        }

        // 保存用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(user.getUid());
        userInfo.setPhone(phone);
        userInfoMapper.insert(userInfo);


        UserAuth userAuth = new UserAuth();
        userAuth.setUid(user.getUid());
        userAuth.setPsw(user.getPsw());
        userAuthMapper.insert(userAuth);


        return generateToken(new User(){{
            setUid(userInfo.getUid());
            setPhone(phone);
        }});
    }

    private String generateToken(User user) throws UnsupportedEncodingException {
        if (user == null)
            return null;

        Map<String, String> claims = new HashMap<>();
        claims.put("uid", String.valueOf(user.getUid()));
        claims.put("phone", String.valueOf(user.getPhone()));

        return JWTUtils.getToken(claims);
    }
}