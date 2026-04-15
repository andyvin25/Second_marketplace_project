package com.marketplace.Auth.domain;

import com.marketplace.RateLimiter.RateLimiterService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@RegisterReflectionForBinding
@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = authHeader.substring(7);
            final String userEmail = jwtUtil.getUsernameFromToken(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtUtil.isTokenValid(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
                if (!rateLimiterService.isAllowed(userEmail)) {
                    throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests");
//                    filterChain.doFilter(request, response);
//                    return;
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            if (exception instanceof ResponseStatusException rse) {
                response.setStatus(rse.getStatusCode().value());
                response.setContentType("application/json");
                response.getWriter().write("rse.getReason()");
                response.getWriter().flush();
            } else if (exception instanceof ExpiredJwtException) {
                log.error("JWT expired: {}", exception.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("Your session has expired. Please log in again.");
                response.getWriter().flush();
            } else if (exception instanceof SignatureException || exception instanceof MalformedJwtException || exception instanceof UnsupportedJwtException) {
                log.error("JWT error: {}", exception.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("Invalid token. Please log in again.");
                response.getWriter().flush();
            } else {
                handlerExceptionResolver.resolveException(request, response, null, exception);
            }
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
