package com.acadlink.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recherches")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Recherche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String ecole;
    private String domaine;
    private Integer duree;
    private String source;

    @Column(columnDefinition = "TEXT")
    private String probleme;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String conclusion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chercheur_id", nullable = false)
    private Chercheur chercheur;
}
