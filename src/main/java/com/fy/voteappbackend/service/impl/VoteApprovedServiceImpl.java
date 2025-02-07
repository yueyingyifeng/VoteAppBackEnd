package com.fy.voteappbackend.service.impl;

import com.fy.voteappbackend.mapper.VoteApprovedMapper;
import com.fy.voteappbackend.model.VoteApproved;
import com.fy.voteappbackend.service.VoteApprovedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteApprovedServiceImpl implements VoteApprovedService {

    @Autowired
    VoteApprovedMapper voteApprovedMapper;

    @Override
    public int setApproved(VoteApproved voteApproved) {
        int row = voteApprovedMapper.insert(voteApproved);
        return row;
    }
}
