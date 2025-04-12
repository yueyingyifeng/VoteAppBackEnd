package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
                setName(userInfo.getName());
            }});
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String register(User user) throws UnsupportedEncodingException {
        if (user == null || user.getPhone() == null || user.getPsw() == null || user.getName() == null)
            return null;

        // 检查手机号是否注册
        String phone = user.getPhone();
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getPhone, phone);
        if (userInfoMapper.selectCount(queryWrapper) > 0) {
            return "phone_exists";
        }

        // 保存用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(user.getUid());
        userInfo.setPhone(phone);
        userInfo.setName(user.getName());
        userInfoMapper.insert(userInfo);

        UserAuth userAuth = new UserAuth();
        userAuth.setUid(user.getUid());
        userAuth.setPsw(user.getPsw());
        userAuthMapper.insert(userAuth);

        return generateToken(new User(){{
            setUid(userInfo.getUid());
            setPhone(phone);
            setName(user.getName());
        }});
    }

    @Override
    public String updatePassword(long uid,String password) {
        UserAuth userAuth = new UserAuth();
        userAuth.setUid(uid);
        userAuth.setPsw(password);
        int row = userAuthMapper.updateById(userAuth);

        if (row == 0)
            return "failed";

        return "success";
    }


    private String generateToken(User user) throws UnsupportedEncodingException {
        if (user == null)
            return null;

        Map<String, String> claims = new HashMap<>();
        claims.put("uid", String.valueOf(user.getUid()));
        claims.put("phone", String.valueOf(user.getPhone()));
        claims.put("name", user.getName());

        return JWTUtils.getToken(claims);
    }
}