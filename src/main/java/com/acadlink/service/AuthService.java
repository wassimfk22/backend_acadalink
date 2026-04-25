package com.acadlink.service;

import com.acadlink.dto.*;
import com.acadlink.entity.*;
import com.acadlink.enums.Role;
import com.acadlink.repository.UtilisateurRepository;
import com.acadlink.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        Utilisateur user = createUserByRole(request);
        user.setNomComplet(request.getNomComplet());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDescription(request.getDescription());
        user.setTitre(request.getTitre());
        user.setRoleUtilisateur(request.getRoleUtilisateur());
        user.setActif(request.getRoleUtilisateur() == Role.ADMIN); // Admin actif par défaut

        Utilisateur saved = utilisateurRepository.save(user);

        String token = jwtUtils.generateToken(saved.getEmail(), saved.getId(), saved.getRoleUtilisateur().name());

        return AuthResponse.builder()
                .token(token)
                .userId(saved.getId())
                .nomComplet(saved.getNomComplet())
                .email(saved.getEmail())
                .role(saved.getRoleUtilisateur())
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.isActif()) {
            throw new RuntimeException("Votre compte n'a pas encore été validé par un administrateur");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String token = jwtUtils.generateToken(user.getEmail(), user.getId(), user.getRoleUtilisateur().name());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .nomComplet(user.getNomComplet())
                .email(user.getEmail())
                .role(user.getRoleUtilisateur())
                .build();
    }

    private Utilisateur createUserByRole(RegisterRequest req) {
        return switch (req.getRoleUtilisateur()) {
            case ETUDIANT -> {
                Etudiant e = new Etudiant();
                e.setObjectif(req.getObjectif());
                yield e;
            }
            case ENSEIGNANT -> {
                Enseignant e = new Enseignant();
                e.setSpecialite(req.getSpecialite());
                e.setObjectif(req.getObjectif());
                yield e;
            }
            case CHERCHEUR -> {
                Chercheur c = new Chercheur();
                c.setDomaine(req.getDomaine());
                c.setObjectif(req.getObjectif());
                c.setDoctorat(req.isDoctorat());
                yield c;
            }
            case ADMIN -> new Administrateur();
        };
    }
}
