package com.marketplace.Store_pages.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketplace.Store_pages.api.StorePageProjection;

@Service("PageStoreService")
public class StoreService {
    
    @Autowired
    private StoreRepository storeRepository;

    public Boolean isStoreWithIdThere(String storeId) {
        return storeRepository.existsById(storeId);
    }

    public Optional<Store> getStoreById(String id) {
        return storeRepository.findById(id);
    }

    public List<StorePageProjection> findPagesWithStoreId(String id) {
        return storeRepository.getPagesWithStoreId(id);
    }

    public void saveStoreWithPages(Store store) {
        Objects.requireNonNull(store);
        storeRepository.save(store);
    }

}
