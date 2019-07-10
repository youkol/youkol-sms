package com.youkol.sms.core.service;

import com.youkol.sms.core.exception.SmsException;

/**
 * 短信账户相关服务接口
 * 
 * @author jackiea
 */
public interface SmsAccountService {

    /**
     * 修改短信通道密码
     * 
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 如果修改成功返回true，否则返回false
     * @throws SmsException 异常信息
     */
    public boolean modifyPassword(String oldPassword, String newPassword) throws SmsException;
}
