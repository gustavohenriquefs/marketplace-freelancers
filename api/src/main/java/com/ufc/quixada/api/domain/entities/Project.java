package com.ufc.quixada.api.domain.entities;

import com.ufc.quixada.api.domain.enums.ExperienceLevel;
import com.ufc.quixada.api.domain.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private Long id;
    private String name;
    private String description;
    private BigDecimal budget;
    private ProjectStatus status;
    private ExperienceLevel experienceLevel;
    private Long deadlineInDays;
    private Boolean isPublic;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private Contractor contractor;
    private Category category;
    private Subcategory subcategory;
    private List<Propose> proposes;
    private List<File> files;
    private List<Skill> skills;
    private List<Freelancer> freelancers;

    public boolean isClosed() {
        return this.status == ProjectStatus.CLOSED;
    }

    public boolean isOnGoing() {
        return this.status == ProjectStatus.IN_PROGRESS || this.status == ProjectStatus.WAITING_PAYMENT;
    }
}