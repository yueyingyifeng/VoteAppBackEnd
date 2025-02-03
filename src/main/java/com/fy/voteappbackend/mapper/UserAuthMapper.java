package com.fy.voteappbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fy.voteappbackend.model.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {
}
