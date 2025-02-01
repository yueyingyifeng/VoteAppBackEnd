package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.VoteParticipation;

public interface VoteParticipationService{

    /**
     * 添加一条 VoteParticipation
     * @param voteParticipation
     * @return
     */
    public int addVoteParticipation(VoteParticipation voteParticipation);
}
