package com.youkol.sms.core.exception;


/**
 * 短信第三方账户余额不足
 *
 * @author jackiea
 */
public class SmsInsufficientException extends SmsException {

    private static final long serialVersionUID = 1L;

    public SmsInsufficientException(String msg) {
        super(msg);
    }

    public SmsInsufficientException(String msg, Exception ex) {
        super(msg, ex);
    }

    public SmsInsufficientException(Exception ex) {
        super(ex);
    }

}
