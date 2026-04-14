package com.marketplace.Auth.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record TokenDto(String userToken, String refreshToken) {
}
