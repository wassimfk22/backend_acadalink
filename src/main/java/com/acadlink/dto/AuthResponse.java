package com.acadlink.dto;

import com.acadlink.enums.Role;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String token;
    private Long userId;
    private String nomComplet;
    private String email;
    private Role role;
}
