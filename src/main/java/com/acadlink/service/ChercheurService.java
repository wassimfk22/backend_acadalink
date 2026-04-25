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

    private Chercheur getChercheur(Long id) {
        Utilisateur u = utilisateurRepository.findById(id).orElseThrow();
        if (!(u instanceof Chercheur c)) throw new RuntimeException("L'utilisateur n'est pas un chercheur");
        return c;
    }

    public List<Recherche> getRecherches(Long chercheurId) {
        return rechercheRepository.findByChercheurId(chercheurId);
    }

    @Transactional
    public Recherche addRecherche(Long chercheurId, Recherche r) {
        r.setChercheur(getChercheur(chercheurId));
        return rechercheRepository.save(r);
    }

    @Transactional
    public void deleteRecherche(Long id) { rechercheRepository.deleteById(id); }
}
