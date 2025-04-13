package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.Votes;

import java.util.List;

public interface AuditService {

    /**
     * 审核通过
     * @param voteId
     * @return
     */
    String passVote(Integer voteId);

    /**
     * 审核不通过
     * @param voteId
     * @return
     */
    String notPassVote(Integer voteId);

    /**
     * 获取审核是否通过的投票项
     * @return
     */
    List<Integer> getIfPassOrElseVoteId(Integer approved);


}
