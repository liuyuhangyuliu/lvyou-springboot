package edu.hit.lvyoubackend.utils;


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
    public static final String EXPIRED_ACCESS_TOKEN = "A016";
    public static final String UNSUPPORTED_TOKEN = "A017";
    public static final String BAD_SIGNATURE_TOKEN = "A018";
    public static final String UNRECONGNIZED_PAYLOAD_USERNAME = "A019";
    public static final String REFRESH_ACCESS_TOKEN = "A020";
    public static final String ACCOUNT_NOT_FOUND_DURING_LOGIN = "A021";
    public static final String VERIFY_CODE_EXPIRED = "A022";
    public static final String INCORRECT_VERIFY_CODE = "A023";
    public static final String ACCOUNT_EXISTS_DURING_REGISTER = "A024";
    public static final String TABLE_FIELD_IS_EMPTY = "A025";

    public static final String APPLY_FOR_CLOSED_SCHEDULE = "A031";
    public static final String APPLY_FOR_OWN_SCHEDULE = "A032";

    public static final String SQLEXCEPTION = "B001";

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
