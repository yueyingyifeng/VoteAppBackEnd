package com.fy.voteappbackend.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fy.voteappbackend.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
