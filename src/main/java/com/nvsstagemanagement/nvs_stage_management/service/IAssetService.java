package com.nvsstagemanagement.nvs_stage_management.service;

import com.nvsstagemanagement.nvs_stage_management.dto.asset.AssetDTO;

import java.util.List;

public interface IAssetService {
    List<AssetDTO> getAllAsset();
    List<AssetDTO> getAssetByName(String name);
    AssetDTO createAsset(AssetDTO assetDTO);
}
