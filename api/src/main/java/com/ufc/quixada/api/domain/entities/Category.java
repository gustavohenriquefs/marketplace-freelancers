package com.ufc.quixada.api.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {
    private Long id;
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
