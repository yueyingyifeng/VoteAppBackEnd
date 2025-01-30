package com.fy.voteappbackend.service.impl;

import com.fy.voteappbackend.mapper.VotesMapper;
import com.fy.voteappbackend.model.Votes;
import com.fy.voteappbackend.service.VotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotesServiceImpl implements VotesService {

    @Autowired
    private VotesMapper votesMapper;

    /**
     *添加投票项
     * @return
     */
    @Override
    public int VotesAdd(Votes vo) {
        return 0;
    }


}
