package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.VoteParticipation;
import com.fy.voteappbackend.model.Votes;

import java.util.List;

public interface VoteParticipationService{

    /**
     * 添加一条 VoteParticipation
     * @param voteParticipation
     * @return
     */
    public int addVoteParticipation(VoteParticipation voteParticipation);


    /**
     * 根据投票项查列表查投票项
     * @param uid
     * @return
     */
    public List<Integer> getParticipationVoteIdList(Long uid);


}
