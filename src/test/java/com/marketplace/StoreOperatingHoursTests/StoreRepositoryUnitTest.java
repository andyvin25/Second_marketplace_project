package com.marketplace.StoreOperatingHoursTests;

import com.marketplace.StoreOperatingHours.domain.Store;
import com.marketplace.StoreOperatingHours.domain.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StoreRepositoryUnitTest {

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void testSaveStoreById() {
        Store store = Store.builder()
                .name("Test Store")
                .build();
        Store savedStore = storeRepository.save(store);

        assertThat(savedStore).isNotNull();
        assertThat(savedStore.getId()).isNotNull();
        assertThat(savedStore.getName()).isEqualTo("Test Store");
    }

//    @Test
//    public void testGetStoreById() {
//        // Arrange: Create and persist a store with a known ID
//        Store store = Store.builder()
//                .name("Test Store")
//                .build();
//
//        // Use merge() if the entity is detached or has a custom ID generator
//        Store managedStore = entityManager.merge(store); // [[5]][[10]]
//        entityManager.flush();
//
//        // Act: Call the repository method
//        Optional<StoreIdDto> result = storeRepository.getStoreById(managedStore.getId());
//
//        // Assert
//        assertThat(result).isPresent();
//        assertThat(result.get().getId()).isEqualTo(managedStore.getId());
//
//    }

}
