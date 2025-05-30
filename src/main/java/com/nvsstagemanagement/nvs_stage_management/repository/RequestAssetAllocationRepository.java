package com.nvsstagemanagement.nvs_stage_management.repository;

import com.nvsstagemanagement.nvs_stage_management.model.RequestAsset;
import com.nvsstagemanagement.nvs_stage_management.model.RequestAssetAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestAssetAllocationRepository extends JpaRepository<RequestAssetAllocation,String> {
    List<RequestAssetAllocation> findByRequestAsset_RequestId(String requestId);
    List<RequestAssetAllocation> findByRequestAsset(RequestAsset request);
    List<RequestAssetAllocation> findByRequestAsset_CreateBy(String createBy);
    Optional<RequestAssetAllocation> findByAsset_AssetIDAndRequestAsset_Task_TaskID(String assetId, String taskId);
}
