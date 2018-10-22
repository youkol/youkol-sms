package com.youkol.sms.core.exception;


/**
 * 批量发送短信时超过允许的最大条数异常信息
 *
 * @author jackiea
 */
public class SmsMaxBatchSizeExceededException extends SmsException {

    private static final long serialVersionUID = 1L;

    public SmsMaxBatchSizeExceededException(String msg) {
        super(msg);
    }

    public SmsMaxBatchSizeExceededException(String msg, Exception ex) {
        super(msg, ex);
    }

    public SmsMaxBatchSizeExceededException(Exception ex) {
        super(ex);
    }

}
