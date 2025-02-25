package com.nvsstagemanagement.nvs_stage_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @Size(max = 50)
    @Nationalized
    @Column(name = "CategoryID", nullable = false, length = 50)
    private String categoryID;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Name", length = 50)
    private String name;

}