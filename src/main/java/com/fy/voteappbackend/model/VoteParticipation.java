package com.fy.voteappbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("vote_participation")
public class VoteParticipation {
    @TableId("uid")
    private Long uid;
    @TableField("vote_id")
    private Integer voteId;
    private Long date;
}
