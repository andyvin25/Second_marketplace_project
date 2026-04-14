//package com.marketplace.Banner.api;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.marketplace.Banner.domain.StoreService;
//
//@RestController
//@RequestMapping("/api")
//public class BannerController {
//
//    private final StoreService storeService;
//
//    @Autowired
//    public BannerController(StoreService storeService) {
//        this.storeService = storeService;
//    }
//
//    @GetMapping("/banner/test")
//    public List<StoreProjection> getAllStoreInfo() {
//        return storeService.getAllStores();
//    }
//
//
//}
