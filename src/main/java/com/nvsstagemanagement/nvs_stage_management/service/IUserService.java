package com.nvsstagemanagement.nvs_stage_management.service;


import com.nvsstagemanagement.nvs_stage_management.dto.request.UserCreationRequest;
import com.nvsstagemanagement.nvs_stage_management.dto.request.UserUpdateRequest;
import com.nvsstagemanagement.nvs_stage_management.dto.user.UserResponse;
import com.nvsstagemanagement.nvs_stage_management.dto.user.UserDTO;

import java.util.List;

public interface IUserService {


    UserResponse createUser(UserCreationRequest request);

    UserDTO getMyInfo();

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);

    UserResponse getUser(String userId);

    List<UserDTO> getUsers();

}
