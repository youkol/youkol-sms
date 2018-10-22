package com.youkol.sms.core.exception;


/**
 * 短信接口不支持的操作异常
 *
 * @author jackiea
 */
public class SmsUnsupportedOperationException extends SmsException {

    private static final long serialVersionUID = 1L;

    public SmsUnsupportedOperationException(String msg) {
        super(msg);
    }

    public SmsUnsupportedOperationException(String msg, Exception ex) {
        super(msg, ex);
    }

    public SmsUnsupportedOperationException(Exception ex) {
        super(ex);
    }

}
