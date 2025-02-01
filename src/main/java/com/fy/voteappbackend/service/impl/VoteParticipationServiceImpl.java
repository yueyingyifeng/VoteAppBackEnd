package com.fy.voteappbackend.service.impl;

import com.fy.voteappbackend.mapper.VoteParticipationMapper;
import com.fy.voteappbackend.model.VoteParticipation;
import com.fy.voteappbackend.service.VoteParticipationService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class VoteParticipationServiceImpl implements VoteParticipationService {

    @Autowired
    VoteParticipationMapper voteParticipationMapper;
    @Override
    public int addVoteParticipation(VoteParticipation voteParticipation) {
        int row = voteParticipationMapper.insert(voteParticipation);
        return row;
    }
}
