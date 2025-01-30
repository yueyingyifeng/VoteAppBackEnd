package com.fy.voteappbackend.mapper;

import com.fy.voteappbackend.model.Votes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VotesMapper {

    @Insert("INSERT INTO votes (title, content, vote_item, public, process_visible, picture_path) " +
            "VALUES" +
            "(#{title},#{content},#{voteItem},#{Public},#{processVisible},#{picture_path})")
    public int VotesAdd(Votes vo);
}
