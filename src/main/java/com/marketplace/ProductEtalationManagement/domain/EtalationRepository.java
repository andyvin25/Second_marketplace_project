package com.marketplace.ProductEtalationManagement.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("EtalationRepository")
public interface EtalationRepository extends JpaRepository<Etalation, String> {
    
}
