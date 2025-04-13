package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.VoteCounts;
import com.fy.voteappbackend.model.Votes;
import lombok.Data;
import org.springframework.stereotype.Service;



public interface VoteCountsService {
    /**
     * 投票
     * @return
     */
    int voteUp(int voteId);


    /**
     * 获取参与人数最多的投票项
     * @return
     */
    VoteCounts getMostHotVoteItemList();

}
