package com.acadlink.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "projets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String sujet;
    private String tuteur;
    private String domaine;
    private Integer duree;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String apprentissage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
}
