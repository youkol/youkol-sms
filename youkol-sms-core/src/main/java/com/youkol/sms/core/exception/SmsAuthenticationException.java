package com.youkol.sms.core.exception;


/**
 * 短信第三方账户密码错误
 *
 * @author jackiea
 */
public class SmsAuthenticationException extends SmsException {

    private static final long serialVersionUID = 1L;

    public SmsAuthenticationException(String msg) {
        super(msg);
    }

    public SmsAuthenticationException(String msg, Exception ex) {
        super(msg, ex);
    }

    public SmsAuthenticationException(Exception ex) {
        super(ex);
    }

}
