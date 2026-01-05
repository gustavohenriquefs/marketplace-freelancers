package com.ufc.quixada.api.infrastructure.models;

import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer deadlineInDays;
    private Boolean isPublic;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    private ContractorJpaModel contractor;

    @ManyToOne (cascade = CascadeType.ALL)
    private CategoryJpaModel category;

    @ManyToOne (cascade = CascadeType.ALL)
    private SubcategoryJpaModel subcategory;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProposeJpaEntity> proposes;

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
