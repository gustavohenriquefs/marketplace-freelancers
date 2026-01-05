package com.ufc.quixada.api.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    private Long id;
    private String name;

    public Skill(String name){
        this.name = name;
    }
}
