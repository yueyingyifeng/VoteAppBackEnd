package com.fy.voteappbackend.model;

import lombok.Data;

import java.util.Map;

@Data
public class GeneralRequest<T> {
    long timeStamp;
    String type;

    T data;
}
