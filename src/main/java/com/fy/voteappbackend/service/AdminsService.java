package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.Admins;
import com.fy.voteappbackend.model.User;

public interface AdminsService {

    /**
     * 管理员登录
     * @return
     */
    String adminsLogin(Admins admins);

    /**
     * 删除投票项
     * @return
     */
    boolean delVoteById(int uId);

    /**
     * 删除用户
     * @return
     */
    int delUserById(User user);
}
