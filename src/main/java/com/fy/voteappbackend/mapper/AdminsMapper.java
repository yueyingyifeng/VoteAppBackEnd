package com.fy.voteappbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fy.voteappbackend.model.Admins;
import com.fy.voteappbackend.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminsMapper extends BaseMapper<Admins> {

    @Select("select * from user_info where uid = #{id};")
    public User selectUserById(long id);

    @Delete("delete from user_info where uid = #{id}")
    public int deleteUserInfoById(long id);

    @Delete("delete from user_auth where uid = #{id}")
    public int deleteUserAuthById(long id);
}
