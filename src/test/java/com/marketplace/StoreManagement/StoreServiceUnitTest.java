package com.marketplace.StoreManagement;

import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.StoreManagement.api.StoreProjection;
import com.marketplace.StoreManagement.domain.Profile;
import com.marketplace.StoreManagement.domain.Store;
import com.marketplace.StoreManagement.domain.StoreRepository;
import com.marketplace.StoreManagement.domain.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StoreServiceUnitTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    private Store store;

    @BeforeEach
    public void setup() {
        String id = "store_id";
        String name= "store_name";

        store = Store.builder()
                .id(id)
                .name(name)
                .build();
    }

    @RepeatedTest(3)
    @Order(1)
    public void saveStore_thenReturnStore() {
        String id = "store_id";
        String name= "store_name";
        when(storeRepository.save(store)).thenReturn(store);

        Store savedStore = storeService.saveStore(store);

        assertThat(savedStore.getId()).isEqualTo(id);
        assertThat(savedStore.getName()).isEqualTo(name);

    }

    @RepeatedTest(3)
    @Order(2)
    public void getStoreById_thenReturnOptionalStore() {
        String id = "store_id";

        when(storeRepository.findById(id)).thenReturn(Optional.ofNullable(store));

        Store existingStore = storeService.getStoreById(id).get();

        assertThat(existingStore).isNotNull();

    }

    @RepeatedTest(3)
    @Order(3)
    public void getAllStores_thenReturnStores() {
        String id = "store_id";
        String name= "store_name";
        String id1 = "store_id1";
        String name1= "store_name1";

        Store store1 = Store.builder()
                .id(id1)
                .name(name1)
                .build();

        when(storeRepository.findAll()).thenReturn(List.of(store, store1));

        List<Store> existingStores = storeService.getAllStores();

        assertThat(existingStores).isNotNull();
        assertThat(existingStores.size()).isGreaterThanOrEqualTo(1);


    }

    @RepeatedTest(3)
    @Order(4)
    public void getAllIdAndNameStoresTest_thenReturnIdAndName() {
        String id = "store_id";
        String name= "store_name";
        String id1 = "store_id1";
        String name1= "store_name1";

        StoreProjection storeDto = new StoreProjection(id, name);
        StoreProjection storeDto1 = new StoreProjection(id1, name1);

        when(storeRepository.getAllStore()).thenReturn(List.of(storeDto, storeDto1));

        List<StoreProjection> storeProjectionsList = storeService.getAllIdAndNameStores();

        assertThat(storeProjectionsList.get(0)).isNotNull();
        assertThat(storeProjectionsList.get(1)).isNotNull();
        assertThat(storeProjectionsList.get(0).storeId()).isEqualTo(id);
        assertThat(storeProjectionsList.get(0).storeName()).isEqualTo(name);
        assertThat(storeProjectionsList.get(1).storeId()).isEqualTo(id1);
        assertThat(storeProjectionsList.get(1).storeName()).isEqualTo(name1);

        assertThat(storeProjectionsList.size()).isGreaterThanOrEqualTo(1);

    }

    @RepeatedTest(3)
    @Order(5)
    public void hasStoreNameSameTest_thenReturnTrue() {

        String id = "store_id";
        String name= "store_name";

        String id1 = "store_id1";
        String name1= "store_name1";

        Store store1 = Store.builder()
                .id(id1)
                .name(name1)
                .build();

        when(storeRepository.findAll()).thenReturn(List.of(store, store1));

        List<Store> stores = storeService.getAllStores();
        Boolean checkStoreName = storeService.hasStoreNameSame(name);
        Boolean checkStoreName1 = storeService.hasStoreNameSame(name1);

        assertThat(checkStoreName).isTrue();
        assertThat(checkStoreName1).isTrue();
        assertThat(stores.get(0).getName().contains(name)).isTrue();

    }

    @RepeatedTest(3)
    @Order(6)
    public void hasStoreNameSameTest_thenReturnFalse() {

        String anotherName= "store_name_another";
        String name= "store_name";

        String id1 = "store_id1";
        String name1= "store_name1";

        Store store1 = Store.builder()
                .id(id1)
                .name(name1)
                .build();

        when(storeRepository.findAll()).thenReturn(List.of(store, store1));

        List<Store> stores = storeService.getAllStores();
        Boolean checkStoreName = storeService.hasStoreNameSame("store_name2");
        assertThat(stores.get(1).getName().contains(anotherName)).isFalse();
        assertThat(checkStoreName).isFalse();

    }

//    @RepeatedTest(3)
//    @Order(7)
//    public void uploadStoreProfileTest_thenThrowResourceNotFoundException() {
//        String id = "store_id1";
//
//        when(storeRepository.findById(id)).thenReturn(Optional.empty());
//
//        MultipartFile file = new MockMultipartFile(
//                "image",
//                "image_test.png",
//                "image/png",
//                "image_test".getBytes()
//        );
//
//        String uploadDir = "test/upload/image/";
//
////        assertThatThrownBy(() -> storeService.uploadStoreProfile(id, file, uploadDir))
////                .isInstanceOf(ResourceNotFoundException.class)
////                .hasMessage("The store with this id not found");
//
//    }

    @RepeatedTest(3)
    public void uploadStoreProfileTest_overLimitedSize_thenThrowIllegalArgumentException () throws IOException {
        String id = "store_id";
        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store existingStore = storeService.getStoreById(id).get();

        byte[] fileSize = new byte[1024 * 1024 * 10];

        MultipartFile file = new MockMultipartFile(
                "image",
                "image_name_test.png",
                "image/png",
                fileSize
        );

        assertThat(existingStore.getId()).isEqualTo(id);

        String uploadDir = "test/upload/image/";
        Path mockPath = Paths.get(uploadDir)
                .resolve(id)
                .resolve("profile/profile_" + id + ".png");
        System.out.println("mockPath = " + mockPath);

        assertThatThrownBy(() -> storeService.uploadStoreProfile(existingStore, file, uploadDir))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File is too large. The size limit is 2 MB.");


    }


    @RepeatedTest(3)
    public void uploadStoreProfileTest_pngVersion_thenReturnStringPath() throws IOException {
        String id = "store_id";
        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store existingStore = storeService.getStoreById(id).get();

        MultipartFile file = new MockMultipartFile(
                "image",
                "image_name_test.png",
                "image/png",
                "test_image".getBytes()
        );

        assertThat(existingStore.getId()).isEqualTo(id);

        String uploadDir = "test/upload/image/";
        Path mockPath = Paths.get(uploadDir)
                        .resolve(id)
                        .resolve("profile/profile_" + id + ".png");
        System.out.println("mockPath = " + mockPath);


        String uploadedFile = storeService.uploadStoreProfile(existingStore, file, uploadDir);
        System.out.println("uploadedFile = " + uploadedFile);

        assertThat(uploadedFile).isNotNull();
        assertThat(uploadedFile).isEqualTo(mockPath.toString());

    }

    @RepeatedTest(3)
    public void uploadStoreProfileTest_jpegVersion_thenReturnStringPath() throws IOException {
        String id = "store_id";
        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store existingStore = storeService.getStoreById(id).get();

        MultipartFile file = new MockMultipartFile(
                "image",
                "image_name_test.jpeg",
                "image/jpeg",
                "test_image".getBytes()
        );

        assertThat(existingStore.getId()).isEqualTo(id);

        String uploadDir = "test/upload/image/";
        Path mockPath = Paths.get(uploadDir)
                .resolve(id)
                .resolve("profile/profile_" + id + ".jpeg");
        System.out.println("mockPath = " + mockPath);


        String uploadedFile = storeService.uploadStoreProfile(existingStore, file, uploadDir);
        System.out.println("uploadedFile = " + uploadedFile);

        assertThat(uploadedFile).isNotNull();
        assertThat(uploadedFile).isEqualTo(mockPath.toString());

    }

    @RepeatedTest(3)
    public void uploadStoreProfileTest_jpgVersion_thenReturnStringPath() throws IOException {
        String id = "store_id";
        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store existingStore = storeService.getStoreById(id).get();

        MultipartFile file = new MockMultipartFile(
                "image",
                "image_name_test.jpg",
                "image/jpg",
                "test_image".getBytes()
        );

        assertThat(existingStore.getId()).isEqualTo(id);

        String uploadDir = "test/upload/image/";
        Path mockPath = Paths.get(uploadDir)
                .resolve(id)
                .resolve("profile/profile_" + id + ".jpg");
        System.out.println("mockPath = " + mockPath);


        String uploadedFile = storeService.uploadStoreProfile(existingStore, file, uploadDir);
        System.out.println("uploadedFile = " + uploadedFile);

        assertThat(uploadedFile).isNotNull();
        assertThat(uploadedFile).isEqualTo(mockPath.toString());

    }

    @RepeatedTest(3)
    public void getStoreLogoTest_StoreNotFound_thenThrowResourceNotFoundException() {
        String id = "store_id1";

        when(storeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.getStoreLogo(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("The store with this id not found");

    }

    @RepeatedTest(3)
    public void getStoreLogoTest_whenFileNotFound_thenThrowResourceNotFoundException() throws IOException {
        String id = "store_id";

        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store existingStore = storeService.getStoreById(id).get();

        String invalidPath = "/invalid/path/logo.png";

        Profile profile = new Profile(invalidPath, existingStore);
        existingStore.setStoreProfile(profile);

        assertThatThrownBy(() -> storeService.getStoreLogo(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("File is not found");

    }

    @RepeatedTest(3)
    public void getStoreLogoTest_thenReturnStringLogoPath() throws IOException {

        String id = "store_id";

        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store existingStore = storeService.getStoreById(id).get();

        Path tempDir = Files.createTempDirectory("test");
        Path tempFile = Files.createFile(tempDir.resolve("logo.png"));
        String logoPath = tempFile.toString();

        Profile profile = new Profile(logoPath, existingStore);
        existingStore.setStoreProfile(profile);

        assertThat(existingStore.getId()).isEqualTo(id);

        String result = storeService.getStoreLogo(id);

        assertThat(result).isEqualTo(logoPath);

        Files.delete(tempFile);
        Files.delete(tempDir);
    }

    @RepeatedTest(3)
    public void deleteStoreLogoTest_whenStoreByIdNotFound_thenThrowResourceNotFoundException() {
        String id = "store_id1";

        when(storeRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> storeService.getStoreLogo(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("The store with this id not found");

    }

    @RepeatedTest(3)
    public void deleteStoreLogoTest_whenFileNotExist_thenThrowResourceNotFoundException() {
        String id = "store_id";

        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store existingStore = storeService.getStoreById(id).get();

        String invalidPath = "/invalid/path/logo.png";

        Profile profile = new Profile(invalidPath, existingStore);
        existingStore.setStoreProfile(profile);

        assertThatThrownBy(() -> storeService.deleteStoreLogo(existingStore))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("The file is not found");
    }

    @RepeatedTest(3)
    public void deleteStoreLogoTest_whenSuccess_returnStoreProfileNull() throws IOException {
        String id = "store_id";

        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        Store existingStore = storeService.getStoreById(id).get();

        Path tempDir = Files.createTempDirectory("test");
        Path tempFile = Files.createFile(tempDir.resolve("logo.png"));
        String logoPath = tempFile.toString();

        Profile profile = new Profile(logoPath, existingStore);
        existingStore.setStoreProfile(profile);

        storeService.deleteStoreLogo(existingStore);

        assertThat(existingStore.getStoreProfile()).isNull();

        assertThat(Files.exists(tempFile)).isFalse();

        Files.delete(tempDir);
    }
}
