package com.changmin.securewebapp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                            HttpServletResponse response,
                            AuthenticationException authException) throws IOException {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");

                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("error", "Unauthorized");
                responseBody.put("message", "인증이 필요합니다.");

                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}