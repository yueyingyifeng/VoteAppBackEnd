package com.fy.voteappbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fy.voteappbackend.mapper.AuditMapper;
import com.fy.voteappbackend.model.Audit;
import com.fy.voteappbackend.model.Votes;
import com.fy.voteappbackend.service.AuditService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    AuditMapper auditMapper;

    private static final Integer APPROVED = 1;
    private static final Integer NOT_APPROVED = 0;

    AuditServiceImpl(AuditMapper auditMapper){
        this.auditMapper = auditMapper;
    }

    /**
     * 审核通过
     * @param voteId
     * @return
     */
    @Override
    public String passVote(Integer voteId) {

        Audit audit = new Audit();
        audit.setVoteId(voteId);
        audit.setApproved(APPROVED);
        auditMapper.updateById(audit);

        return "success";
    }

    /**
     * 审核不通过
     * @param voteId
     * @return
     */
    @Override
    public String notPassVote(Integer voteId) {

        Audit audit = new Audit();
        audit.setVoteId(voteId);
        audit.setApproved(NOT_APPROVED);
        auditMapper.updateById(audit);

        return "success";
    }

    /**
     * 获取审核是否通过的投票项
     *
     * @return
     */
    @Override
    public List<Integer> getIfPassOrElseVoteId(Integer approved) {

        QueryWrapper<Audit> auditWrapper = new QueryWrapper<>();
        auditWrapper.eq("approved",approved);
        List<Audit> AuditList =  auditMapper.selectList(auditWrapper);
        List<Integer> passVoteIds = new ArrayList<>();
        for(Audit audit : AuditList){
            passVoteIds.add(audit.getVoteId());
        }

        return passVoteIds;
    }




}
