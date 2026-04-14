package com.marketplace.config;

import com.marketplace.Auth.domain.AuthTokenFilter;
import com.marketplace.Auth.domain.JwtUtil;
import com.marketplace.Auth.domain.User;
import com.marketplace.Auth.domain.UserService;
import com.marketplace.RateLimiter.RateLimiterService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.PrintWriter;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoFilterInternalUnitTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @Mock
    private RateLimiterService rateLimiterService;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @InjectMocks
    private AuthTokenFilter jwtAuthFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @Mock
    private User user;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ===================== NO AUTH HEADER =====================
    @Test
    void whenNoAuthHeader_thenSkipFilter() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService, userService);
    }

    @Test
    void whenAuthHeaderNotBearer_thenSkipFilter() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil, userDetailsService, userService);
    }

    // ===================== VALID TOKEN =====================
    @Test
    void whenValidToken_thenSetAuthentication() throws Exception {
        String token = "valid.jwt.token";
        String email = "user@test.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(userService.getActiveUserByEmail(email)).thenReturn(user);
        when(jwtUtil.isTokenValid(token, userDetails)).thenReturn(true);
        when(user.getAuthorities()).thenReturn(Collections.emptyList());
        when(rateLimiterService.isAllowed(email)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getPrincipal()).isEqualTo(userDetails);
        verify(filterChain).doFilter(request, response);
    }

    // ===================== INVALID TOKEN =====================
    @Test
    void whenInvalidToken_thenDoNotSetAuthentication() throws Exception {
        String token = "invalid.jwt.token";
        String email = "user@test.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(userService.getActiveUserByEmail(email)).thenReturn(user);
        when(jwtUtil.isTokenValid(token, userDetails)).thenReturn(false);
        when(rateLimiterService.isAllowed(email)).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNull();
        verify(filterChain).doFilter(request, response);
    }

    // ===================== RATE LIMIT =====================
    @Test
    void whenRateLimitExceeded_thenReturn429() throws Exception {
        String token = "valid.jwt.token";
        String email = "user@test.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(userService.getActiveUserByEmail(email)).thenReturn(user);
        when(jwtUtil.isTokenValid(token, userDetails)).thenReturn(true);
        when(user.getAuthorities()).thenReturn(Collections.emptyList());
        when(rateLimiterService.isAllowed(email)).thenReturn(false);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(writer).write("Too many requests");
        verify(filterChain, never()).doFilter(request, response);
    }

    // ===================== EXCEPTION =====================
    @Test
    void whenJwtServiceThrowsException_thenHandleException() throws Exception {
        String token = "bad.jwt.token";
        RuntimeException exception = new RuntimeException("JWT parse error");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.getUsernameFromToken(token)).thenThrow(exception);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver).resolveException(request, response, null, exception);
        verify(filterChain, never()).doFilter(request, response);
    }

}
