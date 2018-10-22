package com.youkol.sms.api;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.jianzhou.sdk.BusinessService;
import com.youkol.sms.core.SmsSender;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsAuthenticationException;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.exception.SmsInsufficientException;
import com.youkol.sms.core.exception.SmsMaxBatchSizeExceededException;
import com.youkol.sms.core.exception.SmsSendException;
import com.youkol.sms.core.exception.SmsUnsupportedOperationException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;
import com.youkol.sms.core.util.SmsUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 上海建周
 * <p>第三方短信发送接口实现
 * <p>详见：http://www.shjianzhou.com
 * 
 * @author jackiea
 */
@Slf4j
public final class ShJianZhouSenderImpl implements SmsSender {

    private static final String Separator = "||";

    private final BusinessService businessService = new BusinessService();

    @Getter
    private final SmsConfig config;

    public ShJianZhouSenderImpl(SmsConfig config) {
        this.config = config;
        if (!isNullOrEmpty(config.getServerHost())) {
            businessService.setWebService(config.getServerHost());
        }
    }

    /**
     * 发送单条短信
     *
     * @param smsMessage 短信信息
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsInsufficientException 短信账户余额不足异常
     * @throws SmsSendException 短信发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public void send(SmsMessage smsMessage) throws SmsException {
        // 添加短信签名
        String content = SmsUtil.addSignName(config, smsMessage);

        // 发送短信
        int result = 0;
        try {
            result = businessService.sendBatchMessage(
                config.getUsername(), config.getPassword(),
                smsMessage.getPhone(), content);

            log.debug("返回结果：" + result);
        } catch (Exception ex) {
            throw new SmsSendException("发送短信发生异常", ex);
        }

        // 结果处理
        if (result > 0) {
            return;
        }
        switch (result) {
            case 0:
                throw new SmsException("短信发送失败");
            case -1:
                throw new SmsInsufficientException("短信发送失败，账户余额不足");
            case -2:
                throw new SmsAuthenticationException("短信发送失败，账号或密码错误");
            default:
                throw new SmsException("短信发送失败，错误码：" + result);
        }
    }

    /**
     * 批量发送短信（内容部分不相同）
     *
     * @param smsMessages 短信信息集合
     * @throws SmsMaxBatchSizeExceededException 超过最大发送条数异常
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsInsufficientException 短信账户余额不足异常
     * @throws SmsSendException 短信发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public void send(List<SmsMessage> smsMessages) throws SmsException {
        if (smsMessages.size() > config.getBatchMaxCount()) {
            throw new SmsMaxBatchSizeExceededException("超过最大发送条数限制");
        }
        // 合并手机号
        String phone = SmsUtil.joinPhone(smsMessages, Separator);
        // 添加短信签名
        String content = SmsUtil.addSignName(config, smsMessages, Separator);

        // 发送短信
        int result = 0;
        try {
            result = businessService.sendPersonalMessages(
                config.getUsername(), config.getPassword(),
                phone, content);

            log.debug("返回结果：" + result);
        } catch (Exception ex) {
            throw new SmsSendException("批量发送短信发生异常", ex);
        }

        // 结果处理
        if (result > 0) {
            return;
        }
        switch (result) {
            case 0:
                throw new SmsException("短信发送失败");
            case -1:
                throw new SmsInsufficientException("短信发送失败，账户余额不足");
            case -2:
                throw new SmsAuthenticationException("短信发送失败，账号或密码错误");
            default:
                throw new SmsException("短信发送失败，错误码：" + result);
        }
    }

    /**
     * 发送多条短信（相同内容）
     * 如果内容带发送时间，则直接内部调用定时发送接口
     *
     * @param smsBatchMessage 批量短信信息
     * @throws SmsMaxBatchSizeExceededException 超过最大发送条数异常
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsInsufficientException 短信账户余额不足异常
     * @throws SmsSendException 短信发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException {
        if (smsBatchMessage.getPhone().size() > config.getBatchMaxCount()) {
            throw new SmsMaxBatchSizeExceededException("超过最大发送条数限制");
        }

        String content = SmsUtil.addSignName(config, smsBatchMessage);
        String phone = SmsUtil.joinPhone(smsBatchMessage, ";");
        Long sendTime = smsBatchMessage.getSendTime();

        int result = 0;
        try {
            if (sendTime == null) {
                result = businessService.sendBatchMessage(config.getUsername(), config.getPassword(), phone, content);
            } else {
                LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(sendTime), ZoneId.systemDefault());
                String time = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                result = businessService.sendTimelyMessage(config.getUsername(), config.getPassword(), time, phone, content);
            }

            log.debug("返回结果：" + result);
        } catch (Exception ex) {
            throw new SmsSendException("批量发送短信发生异常", ex);
        }

        // 结果处理
        if (result > 0) {
            return;
        }
        switch (result) {
            case 0:
                throw new SmsException("短信发送失败");
            case -1:
                throw new SmsInsufficientException("短信发送失败，账户余额不足");
            case -2:
                throw new SmsAuthenticationException("短信发送失败，账号或密码错误");
            default:
                throw new SmsException("短信发送失败，错误码：" + result);
        }
    }

    /**
     * 查询剩余短信数
     *
     * @return 返回剩余短信条数
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsSendException 短信发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public int getRemainedCount() throws SmsException {
        String result = "";
        try {
            result = businessService.getUserInfo(config.getUsername(), config.getPassword());

            log.debug(result);
        } catch (Exception ex) {
            throw new SmsSendException("查询剩余信息发生异常", ex);
        }

        // 结果处理
        if (isNullOrEmpty(result)){
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
    public int getSendedCount() throws SmsException {
        throw new SmsUnsupportedOperationException("不支持的操作");
    }

    /**
     * 修改短信通道密码
     * 修改成功后，同时修改配置中的密码信息
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改成功返回true，否则返回false
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsSendException 短信发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public boolean modifyPassword(String oldPassword, String newPassword) throws SmsException {
        if (isNullOrEmpty(oldPassword) || !oldPassword.equals(config.getPassword())) {
            throw new SmsAuthenticationException("旧密码错误");
        }
        if (isNullOrEmpty(newPassword)) {
            throw new SmsException("新密码不能为空");
        }

        int result = 0;
        try {
            result = businessService.modifyPassword(config.getUsername(), config.getPassword(), newPassword);

            log.debug("返回结果：" + result);
        } catch (Exception ex) {
            throw new SmsSendException("修改密码过程发生异常" + ex);
        }

        return result == 1;
    }

}
