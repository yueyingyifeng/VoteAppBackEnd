package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fy.voteappbackend.mapper.VotesResponsesMapper;
import com.fy.voteappbackend.model.VoteResponses;
import com.fy.voteappbackend.service.VotesResponsesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotesResponsesServiceImpl implements VotesResponsesService {

    @Autowired
    VotesResponsesMapper votesResponsesMapper;

    /**
     * 新增VotesResponses
     * @return
     */
    @Override
    public int addVotesResponses(VoteResponses voteResponses) {
        return votesResponsesMapper.insert(voteResponses);
    }

    @Override
    public VoteResponses getVotesResponses(int voteId) {
        QueryWrapper<VoteResponses> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vote_id", voteId);
        return votesResponsesMapper.selectOne(queryWrapper);
    }

    @Override
    public int deleteVotesResponses(int id) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vote_id", id);
        int row = votesResponsesMapper.delete(queryWrapper);
        return row;
    }

    /**
     * 根据用户ID获取所有创建的投票
     * @param uid 用户ID
     * @return
     */
    @Override
    public List<VoteResponses> getVotesResponsesByUserId(Long uid) {
        QueryWrapper<VoteResponses> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", uid);
        return votesResponsesMapper.selectList(queryWrapper);
    }
}
