package com.example.blackfriday.config.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JWTFilterTest {

    @InjectMocks
    private JWTFilter jwtFilter;

    @Mock
    private JWTUtil jwtUtil;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(null);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("헤더에 토큰이 없을 때")
    void noTokenInHeader() throws ServletException, IOException {
        jwtFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Bearer이 없을 때")
    void notBearerToken() throws ServletException, IOException {
        String bearerToken = "test-token";
        String token = "test-token";
        request.addHeader("Authorization", bearerToken);


        jwtFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("토큰이 유효하지 않을 때")
    void notValidToken() throws ServletException, IOException {
        String bearerToken = "Bearer test-token";
        String token = "test-token";
        request.addHeader("Authorization", bearerToken);
        when(jwtUtil.isExpired(token)).thenReturn(true);


        jwtFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("토큰이 유효할 때")
    void validToken() throws ServletException, IOException {
        String bearerToken = "Bearer test-token";
        String token = "test-token";
        request.addHeader("Authorization", bearerToken);

        when(jwtUtil.isExpired(token)).thenReturn(false);
        when(jwtUtil.getAuthenticationFromToken(token)).thenReturn(new UsernamePasswordAuthenticationToken(null, null, null));

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

}