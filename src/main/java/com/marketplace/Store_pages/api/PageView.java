package com.marketplace.Store_pages.api;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

@RegisterReflectionForBinding
public record PageView(String pageId, String pageName) {
}
