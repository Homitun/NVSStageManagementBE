package com.nvsstagemanagement.nvs_stage_management.dto.requestAsset;

import com.nvsstagemanagement.nvs_stage_management.dto.asset.AssetDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.task.TaskDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.user.UserDTO;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class RequestAssetDTO {
    private String requestId;
    private Integer quantity;
    private String description;
    private Instant startTime;
    private Instant endTime;
    private AssetDTO asset;
    private TaskDTO task;
    private String status;
    private UserDTO requesterInfo;
    private List<RequestAssetCategoryDTO> categories;
}
