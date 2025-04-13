package com.fy.voteappbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fy.voteappbackend.model.Audit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditMapper extends BaseMapper<Audit> {
}
