package com.marketplace.Store_pages.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.marketplace.Store_pages.domain.StoreRepository;
import com.marketplace.Store_pages.domain.StoreService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {
    
    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    // @Test
    // public void testGetStoreById() {
    //     String id = "01E6Z8QY0XJZ9Z9Z9Z9Z9Z9Z9Z";
    //     Store mockStoreView = new Store;

    //     // Mockito.when(storeRepository.get).thenReturn(Optional.of(mockStoreView));

    //     // StoreView result = storeService.getStoreDataById(id).orElseThrow(null);

    //     // assertEquals(Optional.of(mockStoreView), result);
        
    //     // verify(storeRepository).getStoreWithId(id);
    // }

}
