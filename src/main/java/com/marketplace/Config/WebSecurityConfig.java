package com.marketplace.Config;

import com.marketplace.Auth.domain.AuthEntryPoint;
import com.marketplace.Auth.domain.AuthTokenFilter;
import com.marketplace.Auth.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public RateLimiterFilter rateLimiterFilter() {
        return new RateLimiterFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        // Updated configuration for Spring Security 6.x
        http
                .headers(headers ->
                        headers.xssProtection(
                                xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                        ).contentSecurityPolicy(
                                cps -> cps.policyDirectives("script-src 'self'")
                        )
                )
                .cors(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(unauthorizedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers("/auth/**",
//                                        "/user/register", "/user/refresh", "/user/logout",
                                                "/api/test/all"

                                                //                                , "/swagger-ui/**" , "/swagger-ui.html" //disable this endpoint if in production
                                                //                                , "/api/users/**", "/api/user/**", "/api/stores/**", "/api/sellers/**", "/api/store/**", "/api/seller/**"
                                                , "/swagger-ui.html", "/v2/api-docs" // for swagger stuff
                                        ).permitAll()
                                        .requestMatchers("/actuator/**").hasAuthority(Role.RoleEnum.ADMIN.name())
                                        .requestMatchers("/api-docs").hasAuthority(Role.RoleEnum.ADMIN.name())
//                                        .requestMatchers("/api/users/**", "/api/user/**").hasAnyAuthority(Role.RoleEnum.BUYER.name(), Role.RoleEnum.SELLER.name())
//                                        .requestMatchers(HttpMethod.POST, "api/sellers/{account_id}/stores").hasAnyAuthority(Role.RoleEnum.BUYER.name(), Role.RoleEnum.ADMIN.name())
//                                        .requestMatchers("/api/stores/**", "/api/sellers/**", "/api/store/**", "/api/seller/**").hasAnyAuthority(Role.RoleEnum.SELLER.name())
//                                        .requestMatchers("/admin/**").hasAnyAuthority(Role.RoleEnum.ADMIN.name())
//                                        .requestMatchers("").has
                                        .anyRequest().authenticated()
                );
        // Add the JWT Token filter before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(rateLimiterFilter(), AuthTokenFilter.class);
        return http.build();
    }
}








//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
////                        .ignoringRequestMatchers("/user/register", "/user/refresh", "/user/logout", "/user/login")
//
//                )
//                .csrf(csrf -> csrf.disable())
//                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(authEntryPoint))
//                .csrf(Customizer.withDefaults())
//                .csrf((csrf) -> csrf
//                        .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
//                )
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
//                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Disable CORS (or configure if needed)

//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(Customizer.withDefaults())
//                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(authEntryPoint))
//                .cors(Customizer.withDefaults())
//                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
//                .authorizeHttpRequests(requests ->
//                                requests.requestMatchers(HttpMethod.POST, "/user/login").permitAll()
//                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
////                                .requestMatchers(HttpMethod.GET, "/csrf/csrf_token").permitAll()
//                                        .requestMatchers(HttpMethod.GET, "/user/csrf_token").permitAll()
//                                        .anyRequest().authenticated()
//                );
//
//        return http.build();
//    }

