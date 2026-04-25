package com.acadlink.service;

import com.acadlink.entity.*;
import com.acadlink.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnseignantService {

    private final ExperienceProfRepository experienceProfRepository;
    private final UtilisateurRepository utilisateurRepository;

    private Enseignant getEnseignant(Long id) {
        Utilisateur u = utilisateurRepository.findById(id).orElseThrow();
        if (!(u instanceof Enseignant e)) throw new RuntimeException("L'utilisateur n'est pas un enseignant");
        return e;
    }

    public List<ExperienceProf> getExperiences(Long enseignantId) {
        return experienceProfRepository.findByEnseignantId(enseignantId);
    }

    @Transactional
    public ExperienceProf addExperience(Long enseignantId, ExperienceProf exp) {
        exp.setEnseignant(getEnseignant(enseignantId));
        return experienceProfRepository.save(exp);
    }

    @Transactional
    public void deleteExperience(Long id) { experienceProfRepository.deleteById(id); }
}
