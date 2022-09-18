package com.febs.common.exception;

/**
 * @description:
 * @date: 2022/9/18
 **/
public class FebsException extends Exception{

    private static final long serialVersionUID = -6916154462432027437L;

    public FebsException(String message){
        super(message);
    }
}