package com.nvsstagemanagement.nvs_stage_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
public class Category {
    @Id
    @Nationalized
    @Column(name = "CategoryID", nullable = false, length = 50)
    private String categoryID;

    @Nationalized
    @Column(name = "Name", length = 50)
    private String name;

}