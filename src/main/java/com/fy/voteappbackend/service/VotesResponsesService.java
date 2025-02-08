package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.VoteResponses;

public interface VotesResponsesService {

    /**
     * 新增VotesResponses
     * @return
     */
    public int addVotesResponses(VoteResponses voteResponses);

    /**
     * 获取投票选项详情
     * @param voteId
     * @return
     */
    public VoteResponses getVotesResponses(int voteId);

    /**
     * 删除投票数据记录
     * @param id
     * @return
     */
    public int deleteVotesResponses(int id);
}
