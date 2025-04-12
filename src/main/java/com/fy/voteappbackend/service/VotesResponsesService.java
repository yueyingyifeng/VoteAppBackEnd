package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.VoteResponses;
import java.util.List;

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

    /**
     * 根据用户ID获取所有创建的投票
     * @param uid 用户ID
     * @return
     */
    public List<VoteResponses> getVotesResponsesByUserId(Long uid);
}
