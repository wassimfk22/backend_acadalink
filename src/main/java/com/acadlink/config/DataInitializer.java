package com.acadlink.config;

import com.acadlink.entity.Administrateur;
import com.acadlink.enums.Role;
import com.acadlink.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initAdmin() {
        return args -> {
            String adminEmail = "admin@acadlink.com";
            if (utilisateurRepository.findByEmail(adminEmail).isEmpty()) {
                Administrateur admin = new Administrateur();
                admin.setNomComplet("Admin");
//                admin.setPrenom("AcadLink");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("Admin@2024!"));
                admin.setRoleUtilisateur(Role.ADMIN);
                admin.setActif(true);
                admin.setTitre("Administrateur principal");
                admin.setDescription("Compte administrateur par défaut");
                utilisateurRepository.save(admin);
                log.info("✅ Compte administrateur créé : admin@acadlink.com / Admin@2024!");
            } else {
                log.info("ℹ️ Compte administrateur déjà existant.");
            }
        };
    }
}
