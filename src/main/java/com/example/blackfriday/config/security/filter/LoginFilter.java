package com.example.blackfriday.config.security.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JWTUtil jwtUtil;

    /*
    인증 처리 위임 로직 구현
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
            String email = jsonNode.get("email").asText();
            String password = jsonNode.get("password").asText();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password, null);
            return authenticationManagerBuilder.getObject().authenticate(token);
        } catch (IOException e) {
            log.info("로그인 오류");
            throw new RuntimeException(e);
        }
    }

    //인증 성공시 호출
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공");
        String accessToken = jwtUtil.createJwt(authResult);
        response.setHeader("Authorization", "Bearer " + accessToken);
        chain.doFilter(request, response);
    }

    //인증 실패 시 호출
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
//        super.unsuccessfulAuthentication(request, response, failed);
    }
}
