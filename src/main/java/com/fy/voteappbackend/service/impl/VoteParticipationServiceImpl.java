package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fy.voteappbackend.mapper.VoteParticipationMapper;
import com.fy.voteappbackend.model.VoteParticipation;
import com.fy.voteappbackend.model.Votes;
import com.fy.voteappbackend.service.VoteParticipationService;
import com.fy.voteappbackend.service.VotesService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class VoteParticipationServiceImpl implements VoteParticipationService {


    @Autowired
    VoteParticipationMapper voteParticipationMapper;
    @Override
    public int addVoteParticipation(VoteParticipation voteParticipation) {
        return voteParticipationMapper.insert(voteParticipation);
    }

    @Override
    public List<Integer> getParticipationVoteIdList(Long uid) {

        //设置查询条件
        QueryWrapper<VoteParticipation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);

        //开始查询
        List<VoteParticipation> votesList = voteParticipationMapper.selectList(queryWrapper);

        //获取投票项id列表
        List<Integer> votesId = new ArrayList<>();
        for (VoteParticipation voteParticipation : votesList) {
            votesId.add(voteParticipation.getVote_id());
        }

        return votesId;
    }


}
