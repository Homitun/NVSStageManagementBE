package com.nvsstagemanagement.nvs_stage_management.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUserDTO {
    private String taskID;
    private String userID;
}
