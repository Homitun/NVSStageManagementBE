package com.nvsstagemanagement.nvs_stage_management.dto.request;


import com.nvsstagemanagement.nvs_stage_management.model.Department;
import com.nvsstagemanagement.nvs_stage_management.model.Role;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 4, message = "EMAIL_INVALID")
    String email ;
    @Size(min = 4, message = "FULLNAME_INVALID")
    String fullName;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;
    LocalDate dayOfBirth;
    Role role;
    String lastName;
    String pictureProfile;

}
