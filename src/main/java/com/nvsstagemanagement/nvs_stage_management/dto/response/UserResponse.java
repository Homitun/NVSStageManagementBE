package com.nvsstagemanagement.nvs_stage_management.dto.response;

import com.nvsstagemanagement.nvs_stage_management.model.Department;
import com.nvsstagemanagement.nvs_stage_management.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String fullName;
    Role role;
    String status;
    Instant createDate;
    String pictureProfile;
    Department department;
    String password;
    LocalDate dayOfBirth;
    String email;

}
