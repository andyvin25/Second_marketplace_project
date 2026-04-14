package com.marketplace.StoreOperatingHours.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StoreDayOperatingHoursRepository extends JpaRepository<StoreDayOperatingHours, StoreDayOperatingHoursKey> {
    List<StoreDayOperatingHours> findAllByStoreId(String storeId);

}
