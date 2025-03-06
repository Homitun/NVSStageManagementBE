package com.nvsstagemanagement.nvs_stage_management.service.impl;

import com.nvsstagemanagement.nvs_stage_management.dto.authentication.AuthenticatedUserDTO;
//import com.nvsstagemanagement.nvs_stage_management.dto.config.JwtGenerator;
//import com.nvsstagemanagement.nvs_stage_management.dto.exception.InvalidValueException;
//import com.nvsstagemanagement.nvs_stage_management.dto.exception.NotFoundException;
import com.nvsstagemanagement.nvs_stage_management.dto.request.UserCreationRequest;
import com.nvsstagemanagement.nvs_stage_management.dto.request.UserUpdateRequest;
import com.nvsstagemanagement.nvs_stage_management.dto.response.UserResponse;
import com.nvsstagemanagement.nvs_stage_management.dto.user.UserDTO;
import com.nvsstagemanagement.nvs_stage_management.exception.AppException;
import com.nvsstagemanagement.nvs_stage_management.exception.ErrorCode;
import com.nvsstagemanagement.nvs_stage_management.mapper.UserMapper;
import com.nvsstagemanagement.nvs_stage_management.model.Role;
import com.nvsstagemanagement.nvs_stage_management.model.User;
import com.nvsstagemanagement.nvs_stage_management.repository.DepartmentRepository;
import com.nvsstagemanagement.nvs_stage_management.repository.RoleRepository;
import com.nvsstagemanagement.nvs_stage_management.repository.UserRepository;
import com.nvsstagemanagement.nvs_stage_management.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nvsstagemanagement.nvs_stage_management.constant.PredefinedRole;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
     private final PasswordEncoder passwordEncoder;


    //    private LoggerService loggerService;
    @Override
    public List<UserDTO> getAllUser() {
        return List.of();
    }

    @Override
    public List<UserDTO> getUserByName(String name) {
        return List.of();
    }

    @Override
    public AuthenticatedUserDTO login(User user) {
        return null;
    }

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findByRoleName(PredefinedRole.STAFF_ROLE);

        user.setRole(role);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }


    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByFullName(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
// to update
//        Role role = roleRepository.findAllById(request.getRole());
//        user.setRole(role);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
