package com.marketplace.ProductEtalationManagement.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.ProductEtalationManagement.domain.Etalation;
import com.marketplace.ProductEtalationManagement.domain.EtalationSerivce;
import com.marketplace.ProductEtalationManagement.domain.Store;
import com.marketplace.ProductEtalationManagement.domain.StoreService;
import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.ResponseHandler;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class StoreEtalationController {
    
    private final StoreService storeService;
    private final EtalationSerivce etalationSerivce;
    private final EtalationMapper mapper;

    @GetMapping("/stores/etalations")
    public ResponseEntity<Object> getAllEtalationFromAStore() {
        log.info("Get /stores/etalations");
        List<EtalationDto> getAllEtalations = etalationSerivce.getAllEtalation().stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
        return ResponseHandler.generateResponse("Get all etalations", HttpStatus.OK, getAllEtalations);

    }

    @PostMapping("/stores/{store_id}/etalations")
    public ResponseEntity<Object> createEtalation(@PathVariable("store_id") String storeId, @RequestBody @Valid EtalationRequestDto etalationDto) {
        log.info("null");
        Store newStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("There is no store with this id"));
        if (etalationSerivce.hasEtalationTheSameName(etalationDto.etalationName())) {
            throw new ResourceDuplicationException("name has already been taken");
        }
        Etalation etalation = mapper.toEtalation(etalationDto);
        etalation.setStore(newStore);
        newStore.addStoreEtalation(etalation);
        storeService.saveEtalationFromStore(newStore);
        EtalationDto output = mapper.toDto(etalation);
        return ResponseHandler.generateResponse("Successfully create Etalations", HttpStatus.CREATED, output);
    }

    @GetMapping("/stores/{store_id}/etalations")
    public ResponseEntity<Object> getAllEtalationFromStoreId(@PathVariable("store_id") String storeId) {
        log.info("GET /api/stores/{store_id}/etalations", "get the etalations from a store");
        Store store = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        List<EtalationDto> outputs = store.getStoreEtalations().stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
        log.info("GET /api/stores/{store_id}/etalations", "get the etalations from a store");
        return ResponseHandler.generateResponse("Successfully get etalations", HttpStatus.OK, outputs);
    }

    @GetMapping("/stores/{store_id}/etalations/{etalation_id}")
    public ResponseEntity<Object> getEtalation(@PathVariable("store_id") String storeId, @PathVariable("etalation_id") String etalationId) {
        log.info("GET /api/stores/{store_id}/etalations/{etalation_id}");
        Boolean isTheStoreThere = storeService.isStoreWithThisIdThere(storeId);
        if (!isTheStoreThere) {
            throw new ResourceNotFoundException("Store Not found");
        }
        Etalation etalation = etalationSerivce.getEtalationById(etalationId)
            .orElseThrow(() -> new ResourceNotFoundException("Etalation not found"));
        EtalationDto outputs = mapper.toDto(etalation);
        return ResponseHandler.generateResponse("Successfully get etalation", HttpStatus.OK, outputs);
    }

    @DeleteMapping("/stores/{store_id}/etalations/{etalation_id}")
    public ResponseEntity<Object> deleteEtalation(@PathVariable("store_id") String storeId, @PathVariable("etalation_id") String etalationId) {
        Boolean isTheStoreThere = storeService.isStoreWithThisIdThere(storeId);
        if (!isTheStoreThere) {
            throw new ResourceNotFoundException("Store Not found");
        }
        Etalation getEtalation = etalationSerivce.getEtalationById(etalationId)
            .orElseThrow(() -> new ResourceNotFoundException("etalation with this id not found"));
        etalationSerivce.deleteEtalation(getEtalation);
        return ResponseHandler.generateResponse("Etalation has been deleted", HttpStatus.OK, "");
    }

}
