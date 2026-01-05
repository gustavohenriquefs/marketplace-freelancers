package com.ufc.quixada.api.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "projects"
)
public class ProjectJpaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal budget;
    private ProjectStatus status;
    private ExperienceLevel experienceLevel;
    private Long deadlineInDays;
    private Boolean isPublic;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "contractor_id")
    private ContractorJpaModel contractor;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaModel category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubcategoryJpaModel subcategory;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProposeJpaEntity> proposes;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<FileJpaModel> files;

    @ManyToMany
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<SkillJpaModel> skills;
}
