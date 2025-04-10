package com.nvsstagemanagement.nvs_stage_management.controller;

import com.nvsstagemanagement.nvs_stage_management.dto.asset.AssetDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.asset.CreateAssetDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.request.ApiResponse;
import com.nvsstagemanagement.nvs_stage_management.dto.user.*;
import com.nvsstagemanagement.nvs_stage_management.model.User;
import com.nvsstagemanagement.nvs_stage_management.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;

     @PostMapping
     public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserCreationRequest request) {
         UserDTO createdUser = userService.createUser(request);
         return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
     }

    @PutMapping("/activation-user")
    ApiResponse<UserResponse> activationUser( @RequestBody ActivationUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.activationUser(request))
                .build();
    }

    @GetMapping("/get-all")
    ApiResponse<List<UserDTO>> getListUsers() {
        return ApiResponse.<List<UserDTO>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserDTO> getMyInfo() {
        return ApiResponse.<UserDTO>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
    @GetMapping("/department")
    public ResponseEntity<List<UserDTO>> getUsersByDepartmentId(@RequestParam String Id) {
        List<UserDTO> users = userService.getUsersByDepartmentId(Id);
        return ResponseEntity.ok(users);
    }
}
