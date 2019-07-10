package com.youkol.sms.jianzhou.service;

import com.google.common.base.Strings;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsAuthenticationException;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.exception.SmsSendException;
import com.youkol.sms.core.service.SmsAccountService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jackiea
 */
@Slf4j
public class ShJianZhouSmsAccountService extends AbstractShJianZhouSmsService implements SmsAccountService {

    public ShJianZhouSmsAccountService(SmsConfig config) {
        super(config);
    }

    /**
     * 修改短信通道密码 修改成功后，同时修改配置中的密码信息
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @param Strings
     * @return 修改成功返回true，否则返回false
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsSendException           短信发送异常
     * @throws SmsException               短信通用异常
     */
    @Override
    public boolean modifyPassword(String oldPassword, String newPassword) throws SmsException {
        if (Strings.isNullOrEmpty(oldPassword) || !oldPassword.equals(this.getConfig().getPassword())) {
            throw new SmsAuthenticationException("旧密码错误");
        }
        if (Strings.isNullOrEmpty(newPassword)) {
            throw new SmsException("新密码不能为空");
        }

        int result = 0;
        try {
            result = this.getJianZhouService().modifyPassword(
                this.getConfig().getUsername(), 
                this.getConfig().getPassword(), 
                newPassword);

            log.debug("返回结果：" + result);
        } catch (Exception ex) {
            throw new SmsSendException("修改密码过程发生异常" + ex);
        }

        return result == 1;
    }

    
}
