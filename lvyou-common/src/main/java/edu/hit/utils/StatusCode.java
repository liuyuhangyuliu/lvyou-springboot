package edu.hit.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public enum StatusCode {

    OK("000","SUCCESS"),
    ERROR();


    private String code;

    private String msg;


    StatusCode(){

    }

    public StatusCode setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public StatusCode set(String code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }
}
