package com.venble.boot.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Collection;
import java.util.List;

/**
 * Json 工具类
 */
public final class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * 对象转换为 json 字符串
     *
     * @param object 对象
     * @return json 字符串
     */
    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json 字符串转换为对象
     *
     * @param jsonStr json 字符串
     * @param clazz   对象类型
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json 字符串转换为对象集合
     *
     * @param jsonStr json 字符串
     * @param clazz   对象类型
     */
    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
