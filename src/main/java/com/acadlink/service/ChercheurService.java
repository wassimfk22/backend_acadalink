package com.acadlink.service;

import com.acadlink.entity.*;
import com.acadlink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChercheurService {

    private final RechercheRepository rechercheRepository;
    private final UtilisateurRepository utilisateurRepository;

    private Utilisateur getUtilisateur(Long id) {
        Utilisateur u = utilisateurRepository.findById(id).orElseThrow();
        if (!(u instanceof Chercheur c) || !(u instanceof Enseignant e)) throw new RuntimeException("L'utilisateur n'est pas un chercheur ni enseignant");
        return u;
    }

    public List<Recherche> getRecherches(Long chercheurId) {
        return rechercheRepository.findByUtilisateurId(chercheurId);
    }

    @Transactional
    public Recherche addRecherche(Long utilisateurId, Recherche r) {
        r.setUtilisateur(getUtilisateur(utilisateurId));
        return rechercheRepository.save(r);
    }

    @Transactional
    public void deleteRecherche(Long id) { rechercheRepository.deleteById(id); }
}
