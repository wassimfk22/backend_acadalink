package com.acadlink.entity;

import com.acadlink.enums.StatutConnexion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "connexions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Connexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutConnexion statut = StatutConnexion.EN_ATTENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expediteur_id", nullable = false)
    private Utilisateur expediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Utilisateur destinataire;
}
