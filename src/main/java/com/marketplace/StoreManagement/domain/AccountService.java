package com.marketplace.StoreManagement.domain;

import java.util.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.StoreManagement.api.StoreRequestDTO;
import com.marketplace.StoreManagement.domain.StoreDetail.StoreStatusEnum;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }

    public Account saveAccount(Account account) {
        Objects.requireNonNull(account);
        return accountRepository.save(account);
    }

    public void deleteStoreFromAccount(String sellerId) {
        Account account = getAccountById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no user with this id " + sellerId));
        Store store = account.getStores()
                .stream()
                .filter(store1 -> store1.getIsDeleted() == null || !store1.getIsDeleted())
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("There is no user with this id " + sellerId));
        store.setIsDeleted(true);
        saveAccount(account);
    }

    public Store getStoreWithAccount(String id) {
        Account account = accountRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("There is no account with this id " + id ));

        Store store = account.getStores()
                .stream()
                .filter(foundStore -> foundStore.getIsDeleted() == null || !foundStore.getIsDeleted())
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("You need to create store"));
        return store;
    }

    public Store createStore(Account account, Role role, Boolean hasStoreNameSame, StoreRequestDTO storeDto) {

        if (!account.getStores().isEmpty()) {
            throw new ResourceDuplicationException("The store has already created");
        }

        if (hasStoreNameSame) {
            throw new ResourceDuplicationException("The name of the store is already been taken");
        }
        account.getAccountRoles().add(role);

        StoreDetail storeDetail = StoreDetail.builder()
            .rate(0)
            .numberOfSales(0)
            .status(StoreStatusEnum.BRONZE)
            .build();
        Store createdStore = Store.builder()
            .name(storeDto.storeName())
            .storeDetail(storeDetail)
            .isDeleted(false)
            .build();
        Profile storeProfile = new Profile(createdStore);


        createdStore.setStoreProfile(storeProfile);
        storeDetail.setStore(createdStore);
        createdStore.setAccount(account);
        account.getStores().add(createdStore);
        saveAccount(account);
        return createdStore;
    }

    public String updateStoreName(String sellerId, Boolean hasStoreNameSame, StoreRequestDTO storeDto) {
//        Store activeStore = storeRepository.findActiveStoreByAccountId(sellerId)
//            .orElseThrow(() -> new ResourceNotFoundException("Active store not found for this seller"));
        Account account = getAccountById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no user with this id " + sellerId));

        if (hasStoreNameSame) {
            throw new ResourceDuplicationException("The name of the store is already been taken");
        }

        Store store = account.getStores()
                        .stream()
                .filter(foundStore -> foundStore.getIsDeleted() == null || !foundStore.getIsDeleted())
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException("You need to create the store first"));

        store.setName(storeDto.storeName());

        saveAccount(account);

        return store.getName();
    }

    public void deleteAccountAndStore(String sellerId) {
        Account account = getAccountById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller with this id is not found " + sellerId));
        accountRepository.delete(account);
    }

}
