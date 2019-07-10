package com.youkol.sms.core.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.google.common.base.Strings;
import com.youkol.sms.core.config.SmsConfig;
import com.youkol.sms.core.config.SmsConfig.SignPosition;

import lombok.Getter;

/**
 * @author jackiea
 */
public abstract class AbstractSmsSender implements SmsSender {

    @Getter
    private SmsConfig config;

    public AbstractSmsSender(SmsConfig config) {
        this.config = config;
    }

    protected String addSignName(String part) {
        String signName = Strings.nullToEmpty(config.getSignName());
        String result = Strings.nullToEmpty(part);

        SignPosition position = config.getSignPosition();
        if (position == null) {
            return result;
        }

        switch (position) {
            case PREFIX:
                result = signName + result;
            case SUFFIX:
                result = result + signName;
            case NONE:
            default:
                break;
        }

        return result;
    }

    protected String join(String separator, Iterable<String> parts) {
        if (parts == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        parts.forEach(t -> sb.append(separator).append(t));

        if (sb.length() > 0) {
            return sb.substring(separator.length());
        } else {
            return null;
        }
    }

    protected String addSignNameJoin(String separator, Iterable<String> parts) {
        if (parts == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        parts.forEach(t -> sb.append(separator).append(this.addSignName(t)));

        if (sb.length() > 0) {
            return sb.substring(separator.length());
        } else {
            return null;
        }
    }

    protected String formatTime(Long time, String pattern) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
