package com.marketplace.Store_pages.api;

import java.util.List;

import com.marketplace.Store_pages.domain.Page;
import com.marketplace.Store_pages.domain.Store;
import com.marketplace.Store_pages.domain.StorePageService;
import com.marketplace.Store_pages.domain.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.Exception.ResourceDuplicationException;
import com.marketplace.Exception.ResourceNotFoundException;
import com.marketplace.Util.ResponseHandler;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/api")
public class StorePageController {
    
    private final StoreService storeService;
    private final PageMapper mapper;
    private final StorePageService pageService;

    @Autowired
    public StorePageController(StoreService storeService, PageMapper mapper, StorePageService pageService) {
        this.storeService = storeService;
        this.mapper = mapper;
        this.pageService = pageService;
    }

    @Operation(summary = "Retrieve all pages belonging to a store")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieve the store pages",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StorePageProjection.class))
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store not found or no pages available",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"the store with this id not found\"}"
                                    )
                            )
                    }
            )
    })
    @GetMapping("/stores/{store_id}/pages")
    public ResponseEntity<Object> getAllPagesInAStore(@PathVariable("store_id") String storeId) {
        List<StorePageProjection> foundStore = storeService.findPagesWithStoreId(storeId);
        if (foundStore.isEmpty()) {
            throw new ResourceNotFoundException("the store with this id not found");
        }
        return ResponseHandler.generateResponse("Successfully retrive the store pages", HttpStatus.OK, foundStore);
    }

    @Operation(summary = "Create a new page inside a store")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "201", description = "Page successfully created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageDto.class))
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"Store with this id not found\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "409", description = "Page name already exists in this store",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"this kind of name has been listed\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Invalid page name (validation error)",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"page_name\": \"the name shouldn't be empty\"}"
                                    )
                            )
                    }
            )
    })
    @PostMapping("/stores/{store_id}/pages")
    public ResponseEntity<Object> createPage(@PathVariable("store_id") String storeId, @RequestBody @Valid PageRequestDto pageDto) {
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Store with this id not found"));
        Boolean checkPageName = foundStore.getPages().stream()
            .anyMatch(page -> page.getName().equals(pageDto.pageName()));
        if (checkPageName) {
            throw new ResourceDuplicationException("this kind of name has been listed");
        }
        Page page = mapper.toPage(pageDto);
        page.setStore(foundStore);
        foundStore.addPages(page);
        storeService.saveStoreWithPages(foundStore);
        PageDto output = mapper.toDto(page);
        return ResponseHandler.generateResponse("Success for create the store page", HttpStatus.CREATED, output);
    }

    @Operation(summary = "Retrieve specific page inside a store by page id")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Successfully retrieve the page",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StorePageProjection.class))
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store or page not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"The page with this id not found\"}"
                                    )
                            )
                    }
            )
    })
    @GetMapping("/stores/{store_id}/pages/{page_id}")
    public ResponseEntity<Object> getCertainPage(@PathVariable("store_id") String storeId, @PathVariable("page_id") String pageId) {
        List<StorePageProjection> pages = storeService.findPagesWithStoreId(storeId);
        if (pages.isEmpty()) {
            throw new ResourceNotFoundException("the store with this id not found");
        }
        StorePageProjection output = pages.stream()
        .filter(storePageProjection -> storePageProjection.pageId().equals(pageId))
        .findAny()
        .orElseThrow(() -> new ResourceNotFoundException("The page with this id not found"));
        return ResponseHandler.generateResponse("Successfully to get page", HttpStatus.OK, output);
    }

    @Operation(summary = "Update page information inside a store")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Page successfully updated",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageDto.class))
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store or page not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"the page with this id not found\"}"
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "400", description = "Invalid page name (validation error)",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"page_name\": \"the name shouldn't be empty\"}"
                                    )
                            )
                    }
            )
    })
    @PutMapping("/stores/{store_id}/pages/{page_id}")
    public ResponseEntity<Object> updatePage(@PathVariable("store_id") String storeId, @PathVariable("page_id") String pageId, @RequestBody @Valid PageRequestDto pageDto) {
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("the store with this id is not found"));
        Page foundPage = foundStore.getPages().stream()
            .filter(storePage -> storePage.getId().equals(pageId))
            .findAny()
            .orElseThrow(() -> new ResourceNotFoundException("the page with this id not found"));
        pageService.updatePage(foundPage, pageDto);
        storeService.saveStoreWithPages(foundStore);
        PageDto output = mapper.toDto(foundPage);
        return ResponseHandler.generateResponse("Successfully to store id", HttpStatus.OK, output);

    }

    @Operation(summary = "Delete a page from a store")
    @ApiResponses(value = {

            @ApiResponse(responseCode = "200", description = "Page successfully deleted",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = ""
                                    )
                            )
                    }
            ),

            @ApiResponse(responseCode = "404", description = "Store or page not found",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"message\": \"page with this id not found\"}"
                                    )
                            )
                    }
            )
    })
    @DeleteMapping("/stores/{store_id}/pages/{page_id}")
    public ResponseEntity<Object> deletePage(@PathVariable("store_id") String storeId, @PathVariable("page_id") String pageId) {
        Store foundStore = storeService.getStoreById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("store with this id not found"));
        // foundStore.getPages().removeIf(page -> page.getId().equals(pageId));
        boolean checkPageId = foundStore.getPages().stream()
            .anyMatch(page -> page.getId().equals(pageId));
        if (!checkPageId) {
            throw new ResourceNotFoundException("page with this id not found");
        }
        foundStore.getPages().removeIf(page -> page.getId().equals(pageId));
        // foundStore.deletePageWithId(pageId);
        storeService.saveStoreWithPages(foundStore);
        return ResponseHandler.generateResponse("Successfully to delete", HttpStatus.OK, "");
        
    }

    // @GetMapping("/pages/test")
    // public ResponseEntity<Object> pageTestId() {
    //     List<StoreView> stores = storeService.getAllStores();
    //     return ResponseHandler.generateResponse("test id", HttpStatus.OK, stores);
    // }

}
