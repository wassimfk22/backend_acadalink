package com.acadlink.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    // Dossier de stockage — configurable dans application.properties
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    // URL de base pour construire les liens publics
    @Value("${app.upload.base-url:http://localhost:8085}")
    private String baseUrl;

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5 MB
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    /**
     * Sauvegarde un fichier image et retourne son URL publique.
     * Sous-dossier : "publications", "profiles", etc.
     */
    public String store(MultipartFile file, String subFolder) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new RuntimeException("Format non supporté. Utilisez JPEG, PNG, WEBP ou GIF.");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("Fichier trop volumineux (max 5 MB)");
        }

        try {
            // Crée le dossier si nécessaire
            Path targetDir = Paths.get(uploadDir, subFolder).toAbsolutePath().normalize();
            Files.createDirectories(targetDir);

            // Nom unique pour éviter les collisions
            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = getExtension(originalName);
            String fileName = UUID.randomUUID() + "." + extension;

            // Sauvegarde
            Path targetPath = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Retourne l'URL publique
            return baseUrl + "/uploads/" + subFolder + "/" + fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Échec de la sauvegarde du fichier : " + ex.getMessage());
        }
    }

    /**
     * Supprime un fichier à partir de son URL publique.
     */
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;
        try {
            // Extrait le chemin relatif depuis l'URL
            String relativePath = fileUrl.replace(baseUrl + "/uploads/", "");
            Path filePath = Paths.get(uploadDir, relativePath).toAbsolutePath().normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            // On logue mais on ne bloque pas l'opération principale
            System.err.println("Impossible de supprimer le fichier : " + ex.getMessage());
        }
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return (dot >= 0) ? filename.substring(dot + 1).toLowerCase() : "jpg";
    }
    
    
    
}