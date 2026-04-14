package com.marketplace.StoreManagement.api;

import org.springframework.stereotype.Component;

import com.marketplace.StoreManagement.domain.Account;
import com.marketplace.StoreManagement.domain.Store;
import com.marketplace.StoreManagement.domain.StoreDetail;

@Component
public class StoreMapper {
    
    public Store toStore(StoreRequestDTO storeDto, Account account, StoreDetail storeDetail) {
        return Store.builder()
            .name(storeDto.storeName())
            .description(storeDto.description())
            .account(account)
            .storeDetail(storeDetail)
            .build();
    }

    public StoreDto toStoreDto(Store store) {
        return new StoreDto(store.getId(), store.getName(), store.getDescription());
    }

}
