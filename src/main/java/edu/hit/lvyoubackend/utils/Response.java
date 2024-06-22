package edu.hit.lvyoubackend.utils;

import lombok.Data;

@Data
public class Response {

    private String code;

    private String msg;
    private Object data;

    public Response(StatusCode statusCode, Object data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }
}
