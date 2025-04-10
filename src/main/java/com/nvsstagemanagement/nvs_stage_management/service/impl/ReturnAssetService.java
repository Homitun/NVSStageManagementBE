package com.nvsstagemanagement.nvs_stage_management.service.impl;

import com.nvsstagemanagement.nvs_stage_management.dto.returnAsset.ReturnAssetRequestDTO;
import com.nvsstagemanagement.nvs_stage_management.enums.BorrowedAssetStatus;
import com.nvsstagemanagement.nvs_stage_management.model.*;
import com.nvsstagemanagement.nvs_stage_management.repository.*;
import com.nvsstagemanagement.nvs_stage_management.service.IReturnAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReturnAssetService implements IReturnAssetService {

    private final ReturnedAssetRepository returnedAssetRepository;
    private final AssetRepository assetRepository;
    private final TaskRepository taskRepository;
    private final BorrowedAssetRepository borrowedAssetRepository;
    private final AssetUsageHistoryRepository assetUsageHistoryRepository;

    @Override
    @Transactional
    public void returnAsset(ReturnAssetRequestDTO dto) {

        Asset asset = assetRepository.findById(dto.getAssetID())
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        Task task = taskRepository.findById(dto.getTaskID())
                .orElseThrow(() -> new RuntimeException("Task not found"));


        BorrowedAsset borrowed = borrowedAssetRepository.findByAsset_AssetIDAndTask_TaskIDAndStatus(
                        asset.getAssetID(),
                        task.getTaskID(),
                        BorrowedAssetStatus.IN_USE.name()
                )
                .orElseThrow(() -> new RuntimeException("No active borrowed record found for this asset and task."));

        boolean alreadyReturned = returnedAssetRepository.existsByAssetIDAndTaskID(asset.getAssetID(), task.getTaskID());
        if (alreadyReturned) {
            throw new RuntimeException("Asset already returned for this task.");
        }
        ReturnedAsset returnedAsset = new ReturnedAsset();
        returnedAsset.setReturnedAssetID(UUID.randomUUID().toString());
        returnedAsset.setAssetID(asset);
        returnedAsset.setTaskID(task);
        returnedAsset.setReturnTime(Instant.now());
        returnedAsset.setDescription(dto.getDescription());
        returnedAssetRepository.save(returnedAsset);
        AssetUsageHistory usage = assetUsageHistoryRepository
                .findByAsset_AssetIDAndProject_ProjectID(
                        asset.getAssetID(),
                        task.getMilestone().getProject().getProjectID()
                )
                .orElseThrow(() -> new RuntimeException("Usage history not found."));
        usage.setStatus("Returned");
        assetUsageHistoryRepository.save(usage);
        borrowed.setStatus(BorrowedAssetStatus.RETURNED.name());
        borrowedAssetRepository.save(borrowed);
        asset.setStatus("AVAILABLE");
        assetRepository.save(asset);
    }
}
