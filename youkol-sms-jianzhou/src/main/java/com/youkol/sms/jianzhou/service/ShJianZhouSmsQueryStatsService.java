package com.youkol.sms.jianzhou.service;

import com.google.common.base.Strings;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsAuthenticationException;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.exception.SmsSendException;
import com.youkol.sms.core.exception.SmsUnsupportedOperationException;
import com.youkol.sms.core.service.SmsQueryStatsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jackiea
 */
@Slf4j
public class ShJianZhouSmsQueryStatsService extends AbstractShJianZhouSmsService implements SmsQueryStatsService {

    public ShJianZhouSmsQueryStatsService(SmsConfig config) {
        super(config);
    }

    /**
     * 查询剩余短信数
     *
     * @return 返回剩余短信条数
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsSendException           短信发送异常
     * @throws SmsException               短信通用异常
     */
    @Override
    public int queryRemainedCount() throws SmsException {
        String result = "";
        try {
            result = this.getJianZhouService().getUserInfo(
                this.getConfig().getUsername(), 
                this.getConfig().getPassword());

            log.debug(result);
        } catch (Exception ex) {
            throw new SmsSendException("查询剩余信息发生异常", ex);
        }

        // 结果处理
        if (Strings.isNullOrEmpty(result)){
            throw new SmsException("查询失败");
        }
        if (result.indexOf("<error>") > 0) {
            throw new SmsAuthenticationException("查询失败，账号或密码错误");
        }

        result = result.substring(result.indexOf("<remainFee>") + 11, result.indexOf("</remainFee>"));

        return Integer.parseInt(result);
    }

    /**
     * 查询已发送短信数
     *
     * @return 返回发送短信条数
     * @throws SmsUnsupportedOperationException 接口不支持该操作
     */
    @Override
    public int querySendedCount() throws SmsException {
        throw new SmsUnsupportedOperationException("不支持的操作");
    }
}
