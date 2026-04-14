package com.marketplace.Store_pages.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marketplace.Store_pages.api.StorePageProjection;
import com.marketplace.Store_pages.api.StoreView;

@Repository("PageStoreRepository")
public interface StoreRepository extends JpaRepository<Store, String> {
    
    @Query("SELECT new com.marketplace.Store_pages.api.StoreView(s.id) FROM StorePages s")
    List<StoreView> getAllStoreIds();

    // // just write the page in this me
    // @Query("SELECT new com.marketplace.Store_pages.api.StoreView(s.id, s.page) FROM StorePages s WHERE s.id =: storeId")
    // Optional<StoreView> getStoreWithId(@Param("storeId") String id);

    @Query("SELECT new com.marketplace.Store_pages.api.StorePageProjection(p.id, p.name)" +
    "FROM StorePages s JOIN s.pages p WHERE s.id = :storeId"
    )
    List<StorePageProjection> getPagesWithStoreId(@Param("storeId") String id);

}
