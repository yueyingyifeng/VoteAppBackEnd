package com.fy.voteappbackend.model;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("audit_table")
public class Audit {
    @TableId("vote_id")
    private Integer voteId;
    private Integer approved;
}
