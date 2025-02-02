package com.fy.voteappbackend.model;

import lombok.Data;
@Deprecated
@Data
public class ResponseData<T> {

    private Boolean status = true;
    private int code = 200;
    private String message;
    private T data;

    public static ResponseData<Object> ok(Object data) {
        return new ResponseData<>(data);
    }

    public static ResponseData<Object> ok(Object data, String message) {
        return new ResponseData<>(data, message);
    }

    public static ResponseData<Object> fail(String message, int code) {
        ResponseData<Object> responseData = new ResponseData<>();
        responseData.setCode(code);
        responseData.setMessage(message);
        responseData.setStatus(false);
        responseData.setData(null);
        return responseData;
    }

    public ResponseData() {
        super();
    }

    public ResponseData(T data) {
        super();
        this.data = data;
    }

    public ResponseData(T data,String message) {
        super();
        this.data = data;
        this.message=message;
    }

}
