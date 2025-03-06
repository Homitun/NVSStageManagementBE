package com.nvsstagemanagement.nvs_stage_management.service;



import com.nvsstagemanagement.nvs_stage_management.dto.authentication.AuthenticatedUserDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.request.UserCreationRequest;
import com.nvsstagemanagement.nvs_stage_management.dto.request.UserUpdateRequest;
import com.nvsstagemanagement.nvs_stage_management.dto.response.UserResponse;
import com.nvsstagemanagement.nvs_stage_management.dto.user.UserDTO;
import com.nvsstagemanagement.nvs_stage_management.model.User;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUser();
    List<UserDTO> getUserByName(String name);
    AuthenticatedUserDTO login(User user);
    AuthenticatedUserDTO createUser(UserDTO userDTO);


    UserResponse createUser(UserCreationRequest request);

    UserResponse getMyInfo();

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);

    UserResponse getUser(String userId);

    List<UserResponse> getUsers();
    //AuthenticatedUserDTO login(User user);
}
