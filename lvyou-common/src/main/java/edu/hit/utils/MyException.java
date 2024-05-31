package edu.hit.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyException extends Exception{

    private String msg;
    private String code;

    public MyException(String code , String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
