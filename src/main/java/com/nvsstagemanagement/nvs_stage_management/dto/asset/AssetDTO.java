package com.nvsstagemanagement.nvs_stage_management.dto.asset;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nvsstagemanagement.nvs_stage_management.dto.AssetTypeDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.CategoryDTO;
import com.nvsstagemanagement.nvs_stage_management.model.AssetType;
import com.nvsstagemanagement.nvs_stage_management.model.Category;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {
    private String assetID;
    private String assetName;
    private String model;
    private String code;
    private String description;
    private BigDecimal price;
    private LocalDate buyDate;
    private String status;
    private String location;
    private String createdBy;
    private Integer quantity;
    private String image;
    private CategoryDTO category;
    private AssetTypeDTO assetType;
}
