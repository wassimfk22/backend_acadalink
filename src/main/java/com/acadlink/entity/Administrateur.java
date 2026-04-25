package com.acadlink.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "administrateurs")
@Getter @Setter @NoArgsConstructor
public class Administrateur extends Utilisateur {
}
