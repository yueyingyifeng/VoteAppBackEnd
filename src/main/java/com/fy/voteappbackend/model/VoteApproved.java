package com.fy.voteappbackend.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("audit_table")
public class VoteApproved {

    @TableId("vote_id")
    private Integer voteId;
    private Boolean Approved;

}
