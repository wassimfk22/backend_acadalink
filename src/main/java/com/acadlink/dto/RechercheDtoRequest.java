package com.acadlink.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RechercheDtoRequest {
    private String titre;
    private String domaine;
    private String description;
    private String probleme;
    private String conclusion;
    private String source;
    private String ecole;
    private Integer duree;
}
