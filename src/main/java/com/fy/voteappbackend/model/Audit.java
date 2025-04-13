package com.fy.voteappbackend.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("audit_table")
public class Audit {
    private Integer voteId;
    private Integer approved;
}
