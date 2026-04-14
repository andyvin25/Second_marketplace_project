package com.marketplace.StoreManagement.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record StoreProjection(String storeId, String storeName) {}
