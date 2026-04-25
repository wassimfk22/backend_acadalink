package com.acadlink.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "formations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String ecole;
    private String domaine;
    private Integer duree; // en mois

    @Column(columnDefinition = "TEXT")
    private String description;

    private String apprentissage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
}
