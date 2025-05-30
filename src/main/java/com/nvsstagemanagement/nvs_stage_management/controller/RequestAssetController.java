package com.nvsstagemanagement.nvs_stage_management.controller;

import com.nvsstagemanagement.nvs_stage_management.dto.asset.AssetDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.exception.NotEnoughAssetException;
import com.nvsstagemanagement.nvs_stage_management.dto.request.AllocateAssetDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.request.ApiResponse;
import com.nvsstagemanagement.nvs_stage_management.dto.request.ImageUploadDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.requestAsset.*;
import com.nvsstagemanagement.nvs_stage_management.exception.ApiErrorResponse;
import com.nvsstagemanagement.nvs_stage_management.service.IAllocationService;
import com.nvsstagemanagement.nvs_stage_management.service.IRequestApprovalService;
import com.nvsstagemanagement.nvs_stage_management.service.IRequestAssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/request-asset")
@RequiredArgsConstructor
@Slf4j
public class RequestAssetController {
    private final IRequestAssetService requestAssetService;
    private final IRequestApprovalService requestApprovalService;
    private final IAllocationService allocationService;

    @GetMapping
    public List<RequestAssetDTO> getAllAssets() {
        return requestAssetService.getAllRequest();
    }

    @PostMapping
    public ResponseEntity<List<RequestAssetDTO>> createRequests(@RequestBody List<CreateRequestAssetDTO> dtos) {
        List<RequestAssetDTO> responses = requestAssetService.createRequest(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/requestId")
    public ResponseEntity<RequestAssetDTO> getRequestById(@RequestParam String requestId) {
        RequestAssetDTO requestAssetDTO = requestAssetService.getRequestById(requestId);
        return new ResponseEntity<>(requestAssetDTO, HttpStatus.OK);
    }

    @GetMapping("/by-asset")
    public ResponseEntity<List<RequestAssetDTO>> getRequestsByAsset(@RequestParam String assetId) {
        System.out.println("Received assetId: " + assetId);
        List<RequestAssetDTO> requestList = requestAssetService.getRequestsByAssetId(assetId);
        return new ResponseEntity<>(requestList, HttpStatus.OK);
    }

    @PutMapping("/status")
    public ResponseEntity<RequestAssetDTO> updateRequestAssetStatus(@RequestBody UpdateRequestAssetStatusDTO dto) {
        RequestAssetDTO response = requestAssetService.updateRequestAssetStatus(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/leader/department")
    public ResponseEntity<?> getRequestsForLeader(@RequestParam String id) {
        try {
            List<DepartmentLeaderRequestDTO> dtos = requestAssetService.getDepartmentLeaderRequests(id);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving requests for department " + id + ": " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<RequestAssetDTO>> getMyRequests(@RequestParam String userId) {
        List<RequestAssetDTO> requests = requestAssetService.getRequestsByUser(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/asset-manager")
    public ResponseEntity<?> getRequestsForAssetManager() {
        try {
            List<RequestAssetDTO> requests = requestAssetService.getRequestsForAssetManager();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving asset manager requests: " + e.getMessage());
        }
    }



    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(
            @Valid @RequestBody CreateBookingRequestDTO dto
    ) {
        try {
            List<RequestAssetDTO> created = requestAssetService.createBookingRequests(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.<List<RequestAssetDTO>>builder()
                            .code(1000)
                            .message("Đã tạo thành công " + created.size() + " booking slot")
                            .result(created)
                            .build()
                    );
        } catch (RuntimeException e) {
            log.error("Error creating booking", e);
            return ResponseEntity.badRequest()
                    .body(new ApiErrorResponse("CREATE_BOOKING_ERROR", e.getMessage()));
        }
    }

    @PostMapping("/category")
    public ResponseEntity<?> createCategoryRequest(@Valid @RequestBody CreateCategoryRequestDTO dto) {
        try {
            RequestAssetDTO response = requestAssetService.createCategoryRequest(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{requestId}/accept")
    public ResponseEntity<?> acceptCategoryRequest(@PathVariable String requestId) {
        try {
            RequestAssetDTO dto = requestAssetService.acceptCategoryRequest(requestId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing category request approval: " + e.getMessage());
        }
    }

    @PostMapping("/allocate-assets")
    public ResponseEntity<?> allocateAssets(
            @RequestParam String requestId,
            @RequestBody List<AllocateAssetDTO> allocationDTOs) {
        try {
            RequestAssetDTO responseDto = requestApprovalService.allocateAssets(requestId, allocationDTOs);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/auto-allocate/{requestId}")
    public ResponseEntity<?> autoAllocateAssets(@PathVariable String requestId) {
        try {
            RequestAssetDTO result = requestApprovalService.autoAllocateAssets(requestId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error allocating assets: " + e.getMessage());
        }
    }


    @PutMapping("/{requestId}/{userId}/accept-booking")
    public ResponseEntity<RequestAssetDTO> acceptBooking(@PathVariable String requestId, @PathVariable String userId) {
        try {
            RequestAssetDTO response = requestAssetService.acceptBooking(requestId,userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/by-task/{taskId}")
    public ResponseEntity<List<RequestAssetDTO>> getRequestStatusByTask(@PathVariable String taskId) {
        List<RequestAssetDTO> statuses = requestAssetService.getRequestByTask(taskId);
        if (statuses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(statuses);
    }
    @GetMapping("/{requestId}/check-availability")
    public ResponseEntity<?> checkAssetAvailability(@PathVariable String requestId) {
        CheckAvailabilityResult result = requestAssetService.checkAssetAvailabilityAndReturnAssets(requestId);
        if (!result.isAvailable()) {
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{requestId}/allocated-assets")
    public ResponseEntity<List<AssetDTO>> getAllocatedAssets(@PathVariable String requestId) {
        List<AssetDTO> assets = requestAssetService.getAllocatedAssetsByRequestId(requestId);
        return ResponseEntity.ok(assets);
    }
    @PostMapping("/{allocationId}/upload-before-image")
    public ResponseEntity<String> uploadBeforeImagesFromFirebase(
            @PathVariable String allocationId,
            @RequestBody ImageUploadDTO dto
    ) {
        allocationService.saveBeforeImagesFromFirebase(allocationId, dto.getImageUrls());
        return ResponseEntity.ok("Uploaded BEFORE images from Firebase for allocation: " + allocationId);
    }


}
