package com.fy.voteappbackend.service;

import com.fy.voteappbackend.model.VoteApproved;

public interface VoteApprovedService {


    /**
     * 设置是否审核通过
     * @return
     */
    public int setApproved(VoteApproved vo);
}
