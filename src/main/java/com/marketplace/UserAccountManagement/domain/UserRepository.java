package com.marketplace.UserAccountManagement.domain;

import com.marketplace.UserAccountManagement.api.UserAccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("UserRepository")
public interface UserRepository extends JpaRepository<User, String> {
//    @Query("SELECT new com.marketplace.UserAccountManagement.api.UserAccountDTO(u.id, u.email, u.name) FROM User u WHERE u.id = :id")
//    UserAccountDTO findAccountEmailAndName(@Param("id") String id);

    @Query("SELECT u FROM User_UserAccountManagement u WHERE u.id = :id AND (u.isDeleted = false OR u.isDeleted IS NULL)")
    Optional<User> findActiveById(@Param("id") String id);

    @Query("SELECT COUNT(u) FROM User_UserAccountManagement u WHERE u.email = :email AND (u.isDeleted = false OR u.isDeleted IS NULL)")
    long isDuplicate(String email);

    @Query("SELECT u FROM User_UserAccountManagement u " +
            "WHERE u.email = :email " +
            "AND (u.isDeleted = false OR u.isDeleted IS NULL)")
    Optional<User> findActiveByEmail(@Param("email") String email);
}
