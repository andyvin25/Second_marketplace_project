package com.marketplace.StoreManagement;

import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.StoreManagement.api.StoreRequestDTO;
import com.marketplace.StoreManagement.domain.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.class)
//@TestPropertySource(locations = "classpath:test/resources/application.properties")
@ActiveProfiles("test")
public class AccountServiceUnitTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private Role role1;

    private Account account;
    private Set<Role> roles = new HashSet<Role>();
    private Set<Account> accounts = new HashSet<>();

    @BeforeEach
    public void setup() {
        String id = "test_id";
        String name = "test";
        String email = "test@test";
        String password = "testPassword";
        String roleId = "role_id";
        account = Account.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();

        role1 = Role.builder()
                .id(roleId)
                .roleName(Role.RoleEnum.BUYER)
                .build();
        role1.setSellers(accounts);
        roles.add(role1);
        account.setAccountRoles(roles);
    }

    @RepeatedTest(3)
    @Order(1)
    public void saveAccountTest_thenReturnAccount() {
        when(accountRepository.save(account)).thenReturn(account);

        Account savedAccount = accountService.saveAccount(account);

        assertThat(savedAccount).isNotNull();

    }

    @RepeatedTest(3)
    @Order(2)
    public void getAccountByIdTest_thenReturnOptionalAccount() {
        String sellerId = "test_id";
        when(accountRepository.findById("test_id")).thenReturn(Optional.ofNullable(account));

        Account existingAccount = accountService.getAccountById(sellerId).get();

        System.out.println("existingAccount = " + existingAccount);
        assertThat(existingAccount).isNotNull();
    }



    @RepeatedTest(3)
    @Order(4)
    public void createStoreTest_getAccount_notFound() {
        String id = "test_id1";

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(id)
                .orElseThrow(() -> new ResourceNotFoundException("account with this id is not found" + id)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("account with this id is not found" + id);
    }

    @RepeatedTest(3)
    @Order(3)
    public void createStoreTest_thenReturnStore() {
//        Arrange
        String id = "test_id";
        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test", "This is a store test");

        when(accountRepository.findById("test_id")).thenReturn(Optional.of(account));

        Account existingAccount = accountService.getAccountById(id).get();

        assertThat(existingAccount).isNotNull();
        Role role2 = Role.builder()
                .id("role_test_id2")
                .roleName(Role.RoleEnum.SELLER)
                .build();

        Set<Account> accounts1 = new HashSet<>();
        existingAccount.getAccountRoles().add(role2);
        accounts1.add(existingAccount);
        role2.setSellers(accounts1);

        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);


//        act
        Store createStore = accountService.createStore(account, role2, false, storeDto);

//        verify
        assertThat(existingAccount.getAccountRoles()).isNotEmpty();
        assertThat(existingAccount.getAccountRoles().size()).isGreaterThanOrEqualTo(2);
        assertThat(createStore.getStoreDetail().getRate()).isEqualTo(0);
        assertThat(createStore.getStoreDetail().getNumberOfSales()).isEqualTo(0);
        assertThat(createStore.getStoreDetail().getStatus()).isEqualTo(StoreDetail.StoreStatusEnum.BRONZE);
        assertThat(createStore.getName()).isEqualTo(storeDto.storeName());
        assertThat(createStore.getStoreProfile()).isNotNull();

    }

    @RepeatedTest(3)
    @Order(5)
    public void createStoreTest_ifStoreIsNotNull_thenReturnIllegalArgumentException() {

        String sellerId = "test_id";

        when(accountRepository.findById("test_id")).thenReturn(Optional.ofNullable(account));

        Account existingAccount = accountService.getAccountById(sellerId).get();
        System.out.println("existingAccount = " + existingAccount);
        
//        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test");
        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test", "This is a store test");

        Set<Store> stores = new HashSet<>();
        Store newStore = new Store();
        stores.add(newStore);
        existingAccount.setStores(stores);

        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        accountService.saveAccount(existingAccount);
        when(accountRepository.findById(sellerId)).thenReturn(Optional.of(existingAccount));
        Set<Permission> permissions = new HashSet<>();
        permissions.add(new Permission(Permission.PermissionsEnum.SELLER_CREATE));

        Role role = new Role(Role.RoleEnum.SELLER, permissions);

        // Act & Assert
        assertThatThrownBy(() -> accountService.createStore(account, role,false, storeDto) )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The store has already been created"); // Verify message [[5]]
    }

    @RepeatedTest(3)
    @Order(6)
    public void createStore_hasStoreNameSame_thenThrowResourceDuplicationException() {

        // Arrange
        Boolean hasStoreNameSame = true;
        String sellerId = "test_id";
        String storeName = "store_name_test";

        // Mock account without an existing store
        when(accountRepository.findById(sellerId)).thenReturn(Optional.of(account));

        // Create a StoreRequestDTO with the store name
        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test", "This is a store test");

        Set<Permission> permissions = new HashSet<>();
        permissions.add(new Permission(Permission.PermissionsEnum.SELLER_CREATE));

        Role role = new Role(Role.RoleEnum.SELLER, permissions);
        // Act & Assert
        assertThatThrownBy(() -> accountService.createStore(account, role, hasStoreNameSame, storeDto))
                .isInstanceOf(ResourceDuplicationException.class) // Verify exception type
                .hasMessage("The name of the store is already been taken"); // Verify exception message

    }

    @RepeatedTest(3)
    @Order(7)
    public void updateStoreTest_thenReturnStringStoreName() {
        String id = "test_id";
        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test", "This is a store test");


        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        Account existingAccount = accountRepository.findById(id).get();
        existingAccount.setStores(Set.of(new Store("store_name_test")));
        assertThat(existingAccount).isNotNull();

        existingAccount.getStores().add(new Store("store_name_test_update"));

        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        String updatedAccount = accountService.updateStoreName(id, false, storeDto);

        assertThat(updatedAccount).isEqualTo("store_name_test_update");

    }

    @RepeatedTest(3)
    @Order(8)
    public void updateStoreTest_ifGetAccountNull_thenReturnNotFoundException() {
        String id = "test_id1";

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(id)
                .orElseThrow(() -> new ResourceNotFoundException("account with this id is not found" + id)))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("account with this id is not found" + id);
    }

    @RepeatedTest(3)
    @Order(9)
    public void updateStoreTest_ifStoreIsNull_thenThrowIllegalArgumentException() {

        String sellerId = "test_id";

        when(accountRepository.findById("test_id")).thenReturn(Optional.of(account));

        Account existingAccount = accountService.getAccountById(sellerId).get();
        System.out.println("existingAccount = " + existingAccount);

        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test", "This is a store test");


        assertThat(existingAccount.getStores()).isNull();

        // Act & Assert
        assertThatThrownBy(() -> accountService.updateStoreName(sellerId, false, storeDto) )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Store has not created yet"); // Verify message [[5]]
    }

    @RepeatedTest(3)
    @Order(10)
    public void updateStoreTest_ifStoreHasSameName_thenThrowResourceDuplicationException() {
        String sellerId = "test_id";
        Boolean hasStoreNameSame = true;
        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test", "This is a store test");


        when(accountRepository.findById(sellerId)).thenReturn(Optional.of(account));

        Store newStore = new Store(storeDto.storeName());

        Account existingAccount = accountService.getAccountById(sellerId).get();
        existingAccount.setStores(Set.of(newStore));


        assertThatThrownBy(() -> accountService.updateStoreName(sellerId, hasStoreNameSame, storeDto))
                .isInstanceOf(ResourceDuplicationException.class)
                .hasMessage("The name of the store is already been taken");

    }

    @RepeatedTest(3)
    @Order(11)
    public void doesAccountHaveStoreTest_thenReturnTrue() {
        String sellerId = "test_id";
        Boolean hasStoreNameSame = true;

        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test", "This is a store test");


        when(accountRepository.findById(sellerId)).thenReturn(Optional.of(account));

        Store newStore = new Store(storeDto.storeName());

        Account existingAccount = accountService.getAccountById(sellerId).get();
        existingAccount.setStores(Set.of(newStore));

//        assertThat(accountService. .doesAccountHaveStore(sellerId)).isTrue();

    }

    @RepeatedTest(3)
    @Order(12)
    public void doesAccountHaveStoreTest_thenReturnFalse() {
        String sellerId = "test_id";

        when(accountRepository.findById(sellerId)).thenReturn(Optional.of(account));

        Account existingAccount = accountService.getAccountById(sellerId).get();

//        assertThat(accountService.doesAccountHaveStore(sellerId)).isFalse();

    }

    @RepeatedTest(3)
    @Order(13)
    public void deleteStoreFromAccountTest_thenReturnNothing() {
        String sellerId = "test_id";
        StoreRequestDTO storeDto = new StoreRequestDTO("store_name_test", "This is a store test");

        Store newStore = new Store(storeDto.storeName());

        when(accountRepository.findById(sellerId)).thenReturn(Optional.of(account));

        Account existingAccount = accountService.getAccountById(sellerId).get();
        existingAccount.setStores(Set.of(newStore));

        accountService.deleteStoreFromAccount(sellerId);

        assertThat(existingAccount.getStores()).isNull();

    }

    @RepeatedTest(3)
    @Order(14)
    public void deleteStoreFromAccountTest_whenAccountNotFound_thenThrowResourceNotFoundException() {
        String sellerId = "invalid_test_id";

        when(accountRepository.findById(sellerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.deleteStoreFromAccount(sellerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Seller with this id not found");

    }

    @RepeatedTest(3)
    @Order(15)
    public void deleteStoreFromAccountTest_whenStoreNotFound_thenThrowIllegalArgumentException() {
        String sellerId = "invalid_test_id";

        when(accountRepository.findById(sellerId)).thenReturn(Optional.of(account));

        Account existingAccount = accountService.getAccountById(sellerId).get();

        assertThatThrownBy(() -> accountService.deleteStoreFromAccount(sellerId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Store has not created yet");

    }



}
