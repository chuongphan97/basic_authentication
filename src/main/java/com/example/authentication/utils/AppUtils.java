package com.example.authentication.utils;


import com.example.authentication.common.response.PageInfo;
import com.google.gson.JsonObject;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public class AppUtils {

    private AppUtils() {
    }

    /**
     * Create paging with basic information
     *
     * @param <T>
     * @param page {@link Page} of T
     * @return {@link PageInfo}
     */
    public static <T> PageInfo<T> pagingResponse(Page<T> page) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setLimit(page.getSize());
        pageInfo.setPages(page.getTotalPages());
        pageInfo.setPage(page.getNumber() + 1);
        pageInfo.setResult(page.getContent());
        return pageInfo;
    }

    /**
     * Convert LocalDateTime to milliseconds.
     *
     * @param dateTime date time value
     * @return milliseconds
     */
    public static Long convertLocalDateTimeToMilli(LocalDateTime dateTime) {
        if (null != dateTime) {
            return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        return 0L;
    }

    /**
     * Convert milliseconds to LocalDateTime.
     *
     * @param millis milliseconds
     * @return LocalDateTime instance
     */
    public static LocalDateTime convertMilliToLocalDateTime(Long millis) {
        if (null == millis) {
            return null;
        }
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Convert LocalDate to milliseconds.
     *
     * @param dateTime date time value
     * @return milliseconds
     */
    public static Long convertLocalDateToMilli(LocalDate dateTime) {
        if (null != dateTime) {
            return dateTime.atStartOfDay(TimeZone.getDefault().toZoneId()).toInstant().toEpochMilli();
        }
        return 0L;
    }

    public static JsonObject getMessageListFromErrorsValidation(Errors errors) {
        JsonObject jsonObject = new JsonObject();
        try {
            for (ObjectError error : errors.getAllErrors()) {
                String fieldName = ((FieldError) error).getField();
                jsonObject.addProperty(fieldName, error.getDefaultMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}