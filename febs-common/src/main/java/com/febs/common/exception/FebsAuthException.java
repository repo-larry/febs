package com.febs.common.exception;

/**
 * @description:
 * @date: 2022/9/18
 **/
public class FebsAuthException  extends Exception{

    private static final long serialVersionUID = -6916154462432027437L;

    public FebsAuthException(String message){
        super(message);
    }
}