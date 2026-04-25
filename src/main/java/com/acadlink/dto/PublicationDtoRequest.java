package com.acadlink.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class PublicationDtoRequest {

    private String titre;
    private String domaine;
    private String description;
    private String imageUrl;

}