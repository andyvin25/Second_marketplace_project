package com.marketplace.StoreManagement.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("StoreAccountRepository")
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);

    @Query("SELECT u FROM User_StoreManagement u " +
            "LEFT JOIN FETCH u.stores s " +
            "WHERE u.id = :id " +
            "AND (u.isDeleted = false OR u.isDeleted IS NULL)")
    Optional<Account> findActiveById(@Param("id") String id);

}
