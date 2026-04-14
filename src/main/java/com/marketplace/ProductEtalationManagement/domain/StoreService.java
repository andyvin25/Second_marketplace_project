package com.marketplace.ProductEtalationManagement.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("EtalationStoreService")
public class StoreService {
    
    @Autowired
    private StoreRepository storeRepository;

    public List<Store> getAllStore() {
        return storeRepository.findAll();
    }

    public Optional<Store> getStoreById(String id) {
        return storeRepository.findById(id);
    }

    public Boolean isStoreWithThisIdThere(String id) {
        return getAllStore().stream()
            .anyMatch(store -> store.getId().contains(id));
    }

    public void saveEtalationFromStore(Store store) {
        Objects.requireNonNull(store);
        storeRepository.save(store);
    }

}
