//package com.marketplace.Banner.domain;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.marketplace.Banner.api.StoreProjection;
//
//@Repository("BannerStoreRepository")
//public interface StoreRepository extends JpaRepository<Store, String> {
//
//    // @Query(
//    //     "SELECT s.id as storeId FROM BannerStore s"
//    // )
//    // List<StoreProjection> getAllStore();
//
//    @Query("SELECT new com.marketplace.Banner.api.StoreProjection(s.id)" +
//        "FROM BannerStore s"
//    )
//    List<StoreProjection> getAllStore();
//
//}
