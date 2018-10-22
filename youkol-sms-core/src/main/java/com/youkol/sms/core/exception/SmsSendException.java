package com.youkol.sms.core.exception;


/**
 * 短信发送异常
 *
 * @author jackiea
 */
public class SmsSendException extends SmsException {

    private static final long serialVersionUID = 1L;

    public SmsSendException(String msg) {
        super(msg);
    }

    public SmsSendException(String msg, Exception ex) {
        super(msg, ex);
    }

    public SmsSendException(Exception ex) {
        super(ex);
    }

}
