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
import java.util.Set;

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

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "contractor_id", nullable = false)
    private ContractorJpaModel contractor;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaModel category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubcategoryJpaModel subcategory;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProposeJpaEntity> proposes;

    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FileJpaModel> files;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "project_freelancers",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "freelancer_id")
    )
    private Set<FreelancerJpaModel> freelancers;

    @ManyToMany
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<SkillJpaModel> skills;
}
