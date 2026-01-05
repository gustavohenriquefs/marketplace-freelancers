package com.ufc.quixada.api.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Freelancer {
    private Long id;
    private User user;
    private List<Project> projects;
    private List<Propose> proposes;
}