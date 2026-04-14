package com.marketplace.UserAccountManagement;

import com.marketplace.UserAccountManagement.api.UserAccountDTO;
import com.marketplace.UserAccountManagement.domain.UserRepository;
import com.marketplace.UserAccountManagement.domain.User;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AccountRepositoryUnitTest {
    @Autowired
    private UserRepository sellerRepository;

    @RepeatedTest(3)
    @Rollback(value = false)
    public void testSaveAccount_ThenReturnAccountId() {
//        Role role = Role.builder()
//                .roleName(Role.RoleEnum.SELLER)
//                .build();
//        Set<Role> sellerRoles = new HashSet<>();
//        sellerRoles.add(role);
        User seller = new User("test@test", "test", "test_password");
        seller.setCreatedAt(new Date());
        seller.setUpdatedTimes(new Date());
//        sellerInRoles.add(seller);
//        seller.setAccountRoles(sellerRoles);
        User savedSeller = sellerRepository.save(seller);
        System.out.println("savedSeller = " + savedSeller);
        assertThat(savedSeller.getId()).isNotNull();
        assertThat(savedSeller.getEmail()).isEqualTo("test@test");
//        assertThat(savedSeller.getAccountRoles()).isNotNull();
    }

//    @RepeatedTest(3)
//    @Rollback(value = false)
//    public void testFindAccountEmailAndName_thenReturnEmailAndName() {
//        User seller = new User("test@test", "test", "test_password");
//        seller.setCreatedAt(new Date());
//        seller.setUpdatedTimes(new Date());
//        User savedSeller = sellerRepository.save(seller);
//        System.out.println("savedSeller.getName() = " + savedSeller.getName());
//
//        UserAccountDTO result = sellerRepository.fin .findAccountEmailAndName(savedSeller.getId());
////        AccountProjection result = sellerRepository.findAccountEmailAndName(savedSeller.getId());
//
//        assertThat(result).isNotNull();
//    }

}
