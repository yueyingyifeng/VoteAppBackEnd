package com.fy.voteappbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fy.voteappbackend.model.VoteCounts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VoteCountMapper extends BaseMapper<VoteCounts> {

    @Select("SELECT * FROM vote_counts ORDER BY count DESC LIMIT 1")
    public VoteCounts getMostHotVoteItemList();
}
