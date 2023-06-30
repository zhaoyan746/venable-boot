package com.venble.boot.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ServletUtils {

    public static void writeJson(HttpServletResponse response, Object object) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(object));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
