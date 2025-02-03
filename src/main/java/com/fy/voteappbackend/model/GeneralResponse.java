package com.fy.voteappbackend.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.Map;

@Data
public class GeneralResponse {
    long timeStamp;
    String type;
    String err;

    JSONObject data;

    public GeneralResponse makeResponse(String type, String err){
        this.timeStamp = System.currentTimeMillis();
        this.type=type;
        this.err=err;
        return this;
    }

    public GeneralResponse addData(JSONObject data){
        this.data = data;
        return this;
    }
}
