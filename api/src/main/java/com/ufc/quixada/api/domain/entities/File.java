package com.ufc.quixada.api.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private Long id;
    private String fileName;
    private String fileType;
    private String url;

    public File(String fileName, String fileType, String url) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.url = url;
    }
}
