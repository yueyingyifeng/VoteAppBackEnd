package com.fy.voteappbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("admins")
public class Admins {
    @TableField("id")
    private Integer id;
    @TableField("psw")
    private String psw;
}
