package com.nvsstagemanagement.nvs_stage_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
public class BorrowedAsset {
    @Id
    @Size(max = 50)
    @Nationalized
    @Column(name = "BorrowedID", nullable = false, length = 50)
    private String borrowedID;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "AssetID", nullable = false)
    private Asset assetID;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TaskID", nullable = false)
    private Task taskID;

    @NotNull
    @Column(name = "BorowTime", nullable = false)
    private Instant borowTime;

    @NotNull
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @Nationalized
    @Lob
    @Column(name = "Discription")
    private String discription;

}