package com.acadlink.service;

import com.acadlink.entity.*;
import com.acadlink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EtudiantService {

    private final CompetenceRepository competenceRepository;
    private final FormationRepository formationRepository;
    private final ProjetRepository projetRepository;
    private final ExperienceRepository experienceRepository;
    private final UtilisateurRepository utilisateurRepository;

    private Etudiant getEtudiant(Long id) {
        Utilisateur u = utilisateurRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (!(u instanceof Etudiant e)) throw new RuntimeException("L'utilisateur n'est pas un étudiant");
        return e;
    }

    // --- Compétences ---
    public List<Competence> getCompetences(Long etudiantId) { return competenceRepository.findByEtudiantId(etudiantId); }

    @Transactional
    public Competence addCompetence(Long etudiantId, Competence c) {
        c.setEtudiant(getEtudiant(etudiantId));
        return competenceRepository.save(c);
    }

    @Transactional
    public void deleteCompetence(Long id) { competenceRepository.deleteById(id); }

    // --- Formations ---
    public List<Formation> getFormations(Long etudiantId) { return formationRepository.findByEtudiantId(etudiantId); }

    @Transactional
    public Formation addFormation(Long etudiantId, Formation f) {
        f.setEtudiant(getEtudiant(etudiantId));
        return formationRepository.save(f);
    }

    @Transactional
    public void deleteFormation(Long id) { formationRepository.deleteById(id); }

    // --- Projets ---
    public List<Projet> getProjets(Long etudiantId) { return projetRepository.findByEtudiantId(etudiantId); }

    @Transactional
    public Projet addProjet(Long etudiantId, Projet p) {
        p.setEtudiant(getEtudiant(etudiantId));
        return projetRepository.save(p);
    }

    @Transactional
    public void deleteProjet(Long id) { projetRepository.deleteById(id); }

    // --- Expériences ---
    public List<Experience> getExperiences(Long etudiantId) { return experienceRepository.findByEtudiantId(etudiantId); }

    @Transactional
    public Experience addExperience(Long etudiantId, Experience e) {
        e.setEtudiant(getEtudiant(etudiantId));
        return experienceRepository.save(e);
    }

    @Transactional
    public void deleteExperience(Long id) { experienceRepository.deleteById(id); }
}
