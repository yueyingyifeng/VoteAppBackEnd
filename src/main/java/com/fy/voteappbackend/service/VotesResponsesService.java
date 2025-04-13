package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.VoteResponses;
import java.util.List;

public interface VotesResponsesService {

    /**
     * 新增VotesResponses
     * @return
     */
    int addVotesResponses(VoteResponses voteResponses);

    /**
     * 获取投票选项详情
     * @param voteId
     * @return
     */
    VoteResponses getVotesResponses(int voteId);

    /**
     * 删除投票数据记录
     * @param id
     * @return
     */
    int deleteVotesResponses(int id);

    /**
     * 根据用户ID获取所有创建的投票
     * @param uid 用户ID
     * @return
     */
    List<VoteResponses> getVotesResponsesByUserId(Long uid);
}
