package com.marketplace.StoreOperatingHours.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record StoreIdDto(String storeId) {
}