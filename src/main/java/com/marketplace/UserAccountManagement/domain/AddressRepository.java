package com.marketplace.UserAccountManagement.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("AddressRepository")
public interface AddressRepository extends JpaRepository<Address, String> {
}
