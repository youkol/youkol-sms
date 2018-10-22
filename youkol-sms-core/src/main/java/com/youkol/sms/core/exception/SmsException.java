package com.youkol.sms.core.exception;

/**
 * 所有的短信异常基类
 *
 * @author jackiea
 */
public class SmsException extends Exception {

    private static final long serialVersionUID = 1L;

    public SmsException(String msg) {
        super(msg);
    }

    public SmsException(String msg, Exception ex) {
        super(msg, ex);
    }

    public SmsException(Exception ex) {
        super(ex);
    }

}
