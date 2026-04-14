package com.marketplace.Auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("User_Auth_Repository")
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT COUNT(u) FROM User_Auth u WHERE u.email = :email AND (u.isDeleted = false OR u.isDeleted IS NULL)")
    long isDuplicate(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User_Auth u " +
            "LEFT JOIN FETCH u.accountRoles r " +
            "LEFT JOIN FETCH r.permissions " +
            "WHERE u.email = :email " +
            "AND (u.isDeleted = false OR u.isDeleted IS NULL)")
    Optional<User> findActiveByEmail(@Param("email") String email);

}