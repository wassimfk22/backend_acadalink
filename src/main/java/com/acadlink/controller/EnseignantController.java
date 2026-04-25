package com.acadlink.controller;

import com.acadlink.entity.ExperienceProf;
import com.acadlink.security.CustomUserDetails;
import com.acadlink.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enseignant")
@PreAuthorize("hasRole('ENSEIGNANT')")
@RequiredArgsConstructor
public class EnseignantController {

    private final EnseignantService enseignantService;

    @GetMapping("/experiences")
    public ResponseEntity<List<ExperienceProf>> getExperiences(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(enseignantService.getExperiences(user.getId()));
    }

    @PostMapping("/experiences")
    public ResponseEntity<ExperienceProf> addExperience(@AuthenticationPrincipal CustomUserDetails user,
                                                         @RequestBody ExperienceProf exp) {
        return ResponseEntity.ok(enseignantService.addExperience(user.getId(), exp));
    }

    @DeleteMapping("/experiences/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        enseignantService.deleteExperience(id);
        return ResponseEntity.ok().build();
    }
}
