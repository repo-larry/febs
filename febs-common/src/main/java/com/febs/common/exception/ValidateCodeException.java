package com.febs.common.exception;

/**
 * @description:
 * @date: 2022/9/18
 **/
public class ValidateCodeException extends Exception{

    private static final long serialVersionUID = 7514854456967620043L;

    public ValidateCodeException(String message){
        super(message);
    }
}
