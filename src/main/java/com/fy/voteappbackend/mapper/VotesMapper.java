package com.fy.voteappbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fy.voteappbackend.model.VoteCounts;
import com.fy.voteappbackend.model.Votes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface VotesMapper extends BaseMapper<Votes> {

    @Insert("INSERT INTO votes (title, content, vote_item, public, process_visible, picture_path) " +
            "VALUES" +
            "(#{title},#{content},#{voteItem},#{Public},#{processVisible},#{picturePath})")
    public int VotesAdd(Votes vo);


}
