package com.acadlink.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "experiences_prof")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExperienceProf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String ecole;
    private String domaine;
    private String specialite;
    private Integer duree;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enseignant_id", nullable = false)
    private Enseignant enseignant;
}
