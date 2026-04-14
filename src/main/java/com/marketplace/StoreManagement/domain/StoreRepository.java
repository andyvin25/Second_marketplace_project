package com.marketplace.StoreManagement.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marketplace.StoreManagement.api.StoreProjection;

@Repository("StoryRepository_StoreManagement")
public interface StoreRepository extends JpaRepository<Store, String> {
    // @Query("SELECT s.name AS storeName, s.rate AS storeRate FROM Store s")
    // StoreProjection findStoresNameAndRate();
    @Query("SELECT new com.marketplace.StoreManagement.api.StoreProjection(s.id, s.name) FROM Store_StoreManagement s")
    List<StoreProjection> getAllStore();

}
