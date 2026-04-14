package com.marketplace.Store_pages.api;

import org.springframework.stereotype.Component;

import com.marketplace.Store_pages.domain.Page;

@Component
public class PageMapper {
    
    public Page toPage(PageRequestDto pageDto) {
        return new Page(pageDto.pageName());
    }

    public PageDto toDto(Page page) {
        return new PageDto(page.getName());
    }

}
