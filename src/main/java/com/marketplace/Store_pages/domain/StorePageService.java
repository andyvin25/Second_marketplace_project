package com.marketplace.Store_pages.domain;

import java.util.List;
import org.springframework.stereotype.Service;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Store_pages.api.PageRequestDto;
import com.marketplace.Store_pages.api.StorePageProjection;

@Service
public class StorePageService {

    public StorePageProjection getPageWithPageId(String pageId, List<StorePageProjection> pages) {
        return pages.stream()
            .filter(storePageProjection -> storePageProjection.pageId().equals(pageId))
            .findAny()
            .orElseThrow(() -> new ResourceNotFoundException("This page not found"));
    }

    public Page updatePage(Page foundPage, PageRequestDto pageDto) {
        foundPage.setName(pageDto.pageName());
        return foundPage;
    }


}
