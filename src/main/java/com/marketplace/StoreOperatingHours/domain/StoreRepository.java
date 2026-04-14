package com.marketplace.StoreOperatingHours.domain;

import com.marketplace.StoreOperatingHours.api.StoreIdDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("Store_Operating_Hours_Repository")
public interface StoreRepository extends JpaRepository<Store, String> {
    @Query("SELECT new com.marketplace.StoreOperatingHours.api.StoreIdDto(s.id) FROM Store_Operation_Hours s WHERE s.id = :id")
    List<StoreIdDto> getStoreById(@Param("id") String id);
}
