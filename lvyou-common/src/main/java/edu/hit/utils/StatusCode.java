package edu.hit.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public enum StatusCode {



    OK("000","SUCCESS"),
    ERROR();


    public static final String SUCCESS = "000";
    public static final String WRONG_USERNAME_PWD = "A011";
    public static final String OTHER_AUTHC_ERROR = "A012";
    public static final String EMPTY_USERNAME_PWD = "A013";
    public static final String USERNAME_EXISTS = "A014";
    public static final String REGISTER_ERROR = "A015";

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
