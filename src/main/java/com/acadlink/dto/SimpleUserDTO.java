package com.acadlink.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleUserDTO {
    private Long id;
    private String nomComplet;
    private String photoUrl;
}
