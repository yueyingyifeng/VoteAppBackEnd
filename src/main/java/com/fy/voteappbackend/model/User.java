package com.fy.voteappbackend.model;

import lombok.Data;

@Data
public class User {
    long uid;
    String pwd;
    long phone;
}
