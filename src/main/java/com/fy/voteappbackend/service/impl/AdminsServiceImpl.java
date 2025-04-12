package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fy.voteappbackend.Tools.JWTUtils;
import com.fy.voteappbackend.mapper.AdminsMapper;
import com.fy.voteappbackend.model.Admins;
import com.fy.voteappbackend.model.User;
import com.fy.voteappbackend.model.UserAuth;
import com.fy.voteappbackend.model.UserInfo;
import com.fy.voteappbackend.service.AdminsService;
import com.fy.voteappbackend.service.VotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminsServiceImpl implements AdminsService {

    @Autowired
    private AdminsMapper adminsMapper;

    @Autowired
    private VotesService votesService;

    /**
     * 管理员登录
     * @return
     */
    @Override
    public String adminsLogin(Admins admins) {

        if (admins == null || admins.getPsw() == null){
            return null;
        }
        System.out.println(admins);
        LambdaQueryWrapper<Admins> adminsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminsLambdaQueryWrapper.eq(Admins::getId, admins.getId());
        Admins admin = adminsMapper.selectOne(adminsLambdaQueryWrapper);
        System.out.println(admin);
        //判断管理员账号是否输入正确
        if (admin == null) {
            return null;
        }

        //判断密码是否正确
        if (!admin.getPsw().equals(admins.getPsw())) {
            return null;
        }

        try {
            return generateToken(new Admins(){{
                setId(admin.getId());
                setPsw(admin.getPsw());
            }});
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除投票项
     * @return
     */
    @Override
    public boolean delVoteById(int uId) {
        boolean b = votesService.adminsVotesDelete(uId);
        return true;
    }

    /**
     * 删除用户
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)   //开启事务
    public int delUserById(User users) {

        long id = users.getUid();


        //如果用户不存在则返回0表示失败
        User user = adminsMapper.selectUserById(id);
        if (user == null) {
            return 0;
        }


        int userAuthRow = adminsMapper.deleteUserAuthById(id);
        int userInfoRow = adminsMapper.deleteUserInfoById(id);

        //如果两项返回值为0表示用户不存在或删除失败
        if (userAuthRow == 0 && userInfoRow == 0) {
            return 0;
        }

        return userInfoRow;
    }

    private String generateToken(Admins admins) throws UnsupportedEncodingException {
        if (admins == null)
            return null;

        Map<String, String> claims = new HashMap<>();
        claims.put("uid", String.valueOf(admins.getId()));
        claims.put("phone", String.valueOf(admins.getPsw()));

        return JWTUtils.getToken(claims);
    }
}
