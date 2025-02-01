package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fy.voteappbackend.mapper.VoteCountMapper;
import com.fy.voteappbackend.model.VoteCounts;
import com.fy.voteappbackend.service.VoteCountsService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class VoteCountsServiceImpl implements VoteCountsService {

    @Autowired
    private VoteCountMapper voteCountMapper;

    /**
     * 选举项投票数加一
     * @param voteId
     * @return
     */
    @Override
    public int voteUp(int voteId) {
        VoteCounts voteCounts = voteCountMapper.selectById(voteId);
        voteCounts.setCount(voteCounts.getCount()+1);
        voteCounts.setVoteId(voteId);
        int row = voteCountMapper.updateById(voteCounts);
        return row;
    }

    /**
     * 获取参与人数最多的投票项
     * @return
     */
    @Override
    public VoteCounts getMostHotVoteItemList() {
        VoteCounts voteCounts = voteCountMapper.getMostHotVoteItemList();

        return voteCounts;
    }
}
