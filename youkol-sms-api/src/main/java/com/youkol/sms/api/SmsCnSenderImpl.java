package com.youkol.sms.api;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.google.common.hash.Hashing;
import com.youkol.sms.core.SmsSender;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.exception.SmsAuthenticationException;
import com.youkol.sms.core.exception.SmsException;
import com.youkol.sms.core.exception.SmsInsufficientException;
import com.youkol.sms.core.exception.SmsMaxBatchSizeExceededException;
import com.youkol.sms.core.exception.SmsSendException;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;
import com.youkol.sms.core.util.SmsUtil;

import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 云短信
 * <p>第三方短信发送接口实现
 * <p>详见：http://www.sms.cn
 * 
 * @author jackiea
 */
@Slf4j
public final class SmsCnSenderImpl implements SmsSender {

    private static final String Separator = ",";

    private static final String TIME_FORMATTER = "yyyy-MM-dd HH:mm";

    public static final String SEND_SUFFIX = "/mt/";

    public static final String QUERY_SUFFIX = "/mm/";

    public static final String READ_SUFFIX = "/rd/";

    public static final String MODIFY_SUFFIX = "/pw/";

    private static RestTemplate restTemplate = new RestTemplate();

    @Getter
    private final SmsConfig config;

    public SmsCnSenderImpl(SmsConfig config) {
        this.config = config;
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
        // 创建请求地址及参数
        String url = config.getServerHost() + SEND_SUFFIX;
        url = this.createRequestUrl(url, smsMessage);

        // 发送请求
        String response = "";
        try {
            response = restTemplate.postForObject(url, null, String.class);

            log.debug(response);
        } catch (Exception ex) {
            throw new SmsSendException("短信发送异常", ex);
        }

        // 结果处理
        if (isNullOrEmpty(response)){
            throw new SmsException("短信发送失败");
        }

        Map<String, String> result = this.parseResult(response);
        String resultCode = result.get("stat");
        String resultMessage = result.get("message");

        switch (resultCode) {
            case "100":
                return; // 成功
            case "101":
                throw new SmsAuthenticationException("短信发送失败，账号或密码错误");
            case "102":
                throw new SmsInsufficientException("短信发送失败，账户余额不足");
            default:
                resultMessage = String.format("短信发送失败，错误码：%s，错误信息：%s", resultCode, resultMessage);
                throw new SmsException(resultMessage);
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

        for (SmsMessage smsMessage : smsMessages) {
            this.send(smsMessage);
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
     *
     */
    @Override
    public void send(SmsBatchMessage smsBatchMessage) throws SmsException {
        if (smsBatchMessage.getPhone().size() > config.getBatchMaxCount()) {
            throw new SmsMaxBatchSizeExceededException("超过最大发送条数限制");
        }

        // 创建请求地址及参数
        String url = config.getServerHost() + SEND_SUFFIX;
        url = this.createRequestUrl(url, smsBatchMessage);

        // 发送请求
        String response = "";
        try {
            response = restTemplate.postForObject(url, null, String.class);

            log.debug(response);
        } catch (Exception ex) {
            throw new SmsSendException("短信发送异常", ex);
        }

        // 结果处理
        if (isNullOrEmpty(response)){
            throw new SmsException("短信发送失败");
        }

        Map<String, String> result = this.parseResult(response);
        String resultCode = result.get("stat");
        String resultMessage = result.get("message");

        switch (resultCode) {
            case "100":
                return; // 成功
            case "101":
                throw new SmsAuthenticationException("短信发送失败，账号或密码错误");
            case "102":
                throw new SmsInsufficientException("短信发送失败，账户余额不足");
            default:
                resultMessage = String.format("短信发送失败，错误码：%s，错误信息：%s", resultCode, resultMessage);
                throw new SmsException(resultMessage);
        }
    }

    /**
     * 查询剩余短信数
     *
     * @return 返回剩余短信条数
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsSendException 短信请求发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public int getRemainedCount() throws SmsException {
        // 创建请求地址及参数
        String url = config.getServerHost() + QUERY_SUFFIX;
        url = this.createRequestUrl(url, config.getUsername(), config.getPassword());

        // 发送请求
        String response = "";
        try {
            response = restTemplate.postForObject(url, null, String.class);

            log.debug(response);
        } catch (Exception ex) {
            throw new SmsSendException("查询剩余短信数异常", ex);
        }

        // 结果处理
        if (isNullOrEmpty(response)){
            throw new SmsException("查询剩余短信数失败");
        }

        Map<String, String> result = this.parseResult(response);
        String resultCode = result.get("stat");
        String resultMessage = result.get("remain");

        switch (resultCode) {
            case "100":
                return Integer.valueOf(resultMessage); // 成功
            case "101":
                throw new SmsAuthenticationException("查询失败，账号或密码错误");
            default:
                resultMessage = String.format("查询剩余短信数失败，错误码：%s，错误信息：%s", resultCode, resultMessage);
                throw new SmsException(resultMessage);
        }
    }

    /**
     * 查询已发送短信数
     *
     * @return 返回已发送短信条数
     * @throws SmsAuthenticationException 短信账户或密码错误异常
     * @throws SmsSendException 短信请求发送异常
     * @throws SmsException 短信通用异常
     */
    @Override
    public int getSendedCount() throws SmsException {
        // 创建请求地址及参数
        String url = config.getServerHost() + QUERY_SUFFIX;
        url = this.createRequestUrl(url, config.getUsername(), config.getPassword());
        url += "&cmd=send";

        // 发送请求
        String response = "";
        try {
            response = restTemplate.postForObject(url, null, String.class);

            log.debug(response);
        } catch (Exception ex) {
            throw new SmsSendException("查询已发送短信数异常", ex);
        }

        // 结果处理
        if (isNullOrEmpty(response)){
            throw new SmsException("查询已发送短信数失败");
        }

        Map<String, String> result = this.parseResult(response);
        String resultCode = result.get("stat");
        String resultMessage = result.get("remain");

        switch (resultCode) {
            case "100":
                return Integer.valueOf(resultMessage); // 成功
            case "101":
                throw new SmsAuthenticationException("查询失败，账号或密码错误");
            default:
                resultMessage = String.format("查询已发送短信数失败，错误码：%s，错误信息：%s", resultCode, resultMessage);
                throw new SmsException(resultMessage);
        }
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

        // 创建请求地址及参数
        String url = config.getServerHost() + MODIFY_SUFFIX;
        url = this.createRequestUrl(url, config.getUsername(), config.getPassword());
        url += "&newpwd=" + newPassword;

        // 发送请求
        String response = "";
        try {
            response = restTemplate.postForObject(url, null, String.class);

            log.debug(response);
        } catch (Exception ex) {
            throw new SmsSendException("修改短信通道密码异常", ex);
        }

        // 结果处理
        if (isNullOrEmpty(response)){
            throw new SmsException("修改短信通道密码失败");
        }

        Map<String, String> result = this.parseResult(response);
        String resultCode = result.get("stat");
        String resultMessage = result.get("message");

        switch (resultCode) {
            case "100":
                return true; // 成功
            case "101":
                throw new SmsAuthenticationException("修改短信通道密码失败，账号或密码错误");
            default:
                resultMessage = String.format("修改短信通道密码失败，错误码：%s，错误信息：%s", resultCode, resultMessage);
                throw new SmsException(resultMessage);
        }
    }

    /**
     * 创建请求URL
     * 
     * @param url       接口方请求地址
     * @param username  账户名
     * @param password  密码
     * @return 返回请求URl
     */
    @SuppressWarnings("deprecation")
    private String createRequestUrl(String url, String username, String password) {
        StringBuilder result = new StringBuilder();
        result.append(url).append("?");

        String uid = config.getUsername();
        String pwd = config.getPassword();
        pwd = Hashing.md5().hashString(pwd + uid, StandardCharsets.UTF_8).toString();

        String param = String.format("uid=%s&pwd=%s", uid, pwd);

        return result.append(param).toString();
    }

    /**
     * 创建请求URL，包含短信内容
     * 
     * @param url       接口方请求地址
     * @param message   短信信息
     * @return 返回包括短信内容的请求地址
     * @throws SmsException 短信异常信息
     */
    @SuppressWarnings("deprecation")
    private String createRequestUrl(String url, SmsMessage message) throws SmsException {
        StringBuilder result = new StringBuilder();
        result.append(url).append("?");

        String encode = "utf8";
        String uid = config.getUsername();
        String pwd = config.getPassword();
        pwd = Hashing.md5().hashString(pwd + uid, StandardCharsets.UTF_8).toString();
        String mobile = message.getPhone();
        String mobileIds = mobile + System.currentTimeMillis();
        String content = SmsUtil.addSignName(config, message);
        try {
            content = URLEncoder.encode(content, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new SmsException("短信内容转码异常", ex);
        }

        String param = String.format("uid=%s&pwd=%s&mobile=%s&mobileids=%s&content=%s&encode=%s", 
            uid, pwd, mobile, mobileIds, content, encode);

        // 发送时间，如果存在则添加参数，此时为定时发送
        String sendTime = "";
        Long time = message.getSendTime();
        if (time != null) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault());
            sendTime = dateTime.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
        }
        if (!isNullOrEmpty(sendTime)) {
            param = param + "&time=" + sendTime;
        }

        return result.append(param).toString();
    }

    @SuppressWarnings("deprecation")
    private String createRequestUrl(String url, SmsBatchMessage message) throws SmsException {
        StringBuilder result = new StringBuilder();
        result.append(url).append("?");

        String encode = "utf8";
        String uid = config.getUsername();
        String pwd = config.getPassword();
        pwd = Hashing.md5().hashString(pwd + uid, StandardCharsets.UTF_8).toString();
        StringBuilder mobile = new StringBuilder();
        StringBuilder mobileIds = new StringBuilder();
        message.getPhone().forEach(t -> {
            mobile.append(Separator + t);
            mobileIds.append(Separator + t + System.currentTimeMillis());
        });
        String content = SmsUtil.addSignName(config, message);
        try {
            content = URLEncoder.encode(content, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new SmsException("短信内容转码异常", ex);
        }

        String param = String.format("uid=%s&pwd=%s&mobile=%s&mobileids=%s&content=%s&encode=%s", 
            uid, pwd, mobile.substring(1), mobileIds.substring(1), content, encode);

        // 发送时间，如果存在则添加参数，此时为定时发送
        String sendTime = "";
        Long time = message.getSendTime();
        if (time != null) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault());
            sendTime = dateTime.format(DateTimeFormatter.ofPattern(TIME_FORMATTER));
        }
        if (!isNullOrEmpty(sendTime)) {
            param = param + "&time=" + sendTime;
        }

        return result.append(param).toString();
    }

    /**
     * <p>解析返回结果
     * <p>返回结果形如：<code>sms&stat=100&message=发送成功</code>
     * <p>stat 状态码
     * <p>message 状态说明
     * 
     * @param reponse 返回字符串
     * @return 返回解析后的结果
     */
    private Map<String, String> parseResult(String reponse) {
        if (isNullOrEmpty(reponse)) {
            return Collections.<String, String>emptyMap();
        }
        Map<String, String> result;
        Splitter.MapSplitter mapSplitter = Splitter.on("&").trimResults().withKeyValueSeparator("=");
        result = mapSplitter.split(reponse.substring(reponse.indexOf("&") + 1));

        return result;
    }

}
