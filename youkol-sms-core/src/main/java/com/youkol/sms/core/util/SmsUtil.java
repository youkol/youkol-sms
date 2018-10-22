package com.youkol.sms.core.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.model.SmsBatchMessage;
import com.youkol.sms.core.model.SmsMessage;

/**
 * 短信工具类
 * 
 * @author jackiea
 */
public class SmsUtil {

    /**
     * 添加短信签名
     * 
     * @param config    短信配置信息
     * @param content   短信内容
     * @return 返回根据短信签名位置进行签名后的短信内容
     */
    public static String addSignName(SmsConfig config, String content) {
        String result = content;
        if (config.getSignPosition() == null) {
            return result;
        }

        switch (config.getSignPosition()) {
            case NONE:
                break;
            case PREFIX:
                result = Strings.nullToEmpty(config.getSignName()) + result;
            case SUFFIX:
                result = result + Strings.nullToEmpty(config.getSignName());
            default:
                break;
        }

        return result;
    }

    /**
     * 添加短信签名
     * 
     * @param config    短信配置信息
     * @param message   短信信息
     * @return 返回根据短信签名位置进行签名后的短信内容
     */
    public static String addSignName(SmsConfig config, SmsMessage message) {
        return addSignName(config, message.getContent());
    }

    /**
     * 添加短信签名
     * 
     * @param config    短信配置信息
     * @param messages  短信信息集合
     * @return 返回根据短信签名位置进行签名后的短信内容，集合顺序与原有顺序保持一致
     */
    public static List<String> addSignName(SmsConfig config, List<SmsMessage> messages) {
        List<String> contents = new ArrayList<>();
        messages.forEach(t -> {
            contents.add(addSignName(config, t));
        });

        return contents;
    }

    /**
     * 添加短信签名，并使用分隔符合并短信内容
     * 
     * @param config    短信配置信息
     * @param messages  短信信息集合
     * @param separator 分隔符，合并短信内容使用
     * @return 返回根据短信签名位置进行签名后，并使用分隔符合并的短信内容
     */
    public static String addSignName(SmsConfig config, List<SmsMessage> messages, String separator) {
        List<String> contents = addSignName(config, messages);

        StringBuilder content = new StringBuilder();
        contents.forEach(t -> {
            content.append(t).append(separator);
        });

        return content.substring(0, content.length() - separator.length());
    }

    /**
     * 添加短信签名
     * 
     * @param config        短信配置信息
     * @param batchMessage  批量短信信息
     * @return 返回根据短信签名位置进行签名后的短信内容
     */
    public static String addSignName(SmsConfig config, SmsBatchMessage batchMessage) {
        return addSignName(config, batchMessage.getContent());
    }

    /**
     * 根据分隔符，合并手机号
     * 
     * @param messages  短信信息集合
     * @param separator 分隔符，合并手机号使用
     * @return 返回合并后的手机号字符串
     */
    public static String joinPhone(List<SmsMessage> messages, String separator) {
        StringBuilder phone = new StringBuilder();

        messages.forEach(t -> {
            phone.append(t).append(separator);
        });

        return phone.substring(0, phone.length() - separator.length());
    }

    /**
     * 根据分隔符，合并手机号
     * 
     * @param message   批量短信信息
     * @param separator 分隔符，合并手机号使用
     * @return 返回合并后的手机号字符串
     */
    public static String joinPhone(SmsBatchMessage message, String separator) {
        StringBuilder phone = new StringBuilder();

        message.getPhone().forEach(t -> {
            phone.append(t).append(separator);
        });

        return phone.substring(0, phone.length() - separator.length());
    }

}