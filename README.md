# AcadLink Backend — Spring Boot

## Prérequis
- Java 17+
- Maven 3.8+
- PostgreSQL 14+

## Installation

1. Créer la base de données :
```sql
CREATE DATABASE acadlink;
```

2. Configurer `src/main/resources/application.properties` (username/password PostgreSQL)

3. Lancer :
```bash
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`

## Structure du projet

```
com.acadlink
├── config/          # SecurityConfig, WebSocketConfig
├── controller/      # REST Controllers (Auth, Publication, Message, etc.)
├── dto/             # Data Transfer Objects
├── entity/          # JPA Entities (Utilisateur, Publication, etc.)
├── enums/           # Role, StatutConnexion
├── exception/       # GlobalExceptionHandler
├── repository/      # Spring Data JPA Repositories
├── security/        # JWT (JwtUtils, Filter, UserDetails)
└── service/         # Business Logic
```

## API Endpoints

### Auth (public)
- `POST /api/auth/register` — Inscription
- `POST /api/auth/login` — Connexion

### Utilisateurs (authentifié)
- `GET /api/utilisateurs/me` — Mon profil
- `GET /api/utilisateurs/{id}` — Profil d'un utilisateur
- `PUT /api/utilisateurs/me` — Modifier mon profil
- `GET /api/utilisateurs/search?q=...` — Rechercher

### Publications (GET public, POST/DELETE authentifié)
- `GET /api/publications` — Fil d'actualité
- `GET /api/publications/{id}` — Détail
- `POST /api/publications` — Créer
- `DELETE /api/publications/{id}` — Supprimer
- `POST /api/publications/{id}/like` — Liker/Unliker
- `GET /api/publications/search?q=...` — Rechercher

### Commentaires
- `GET /api/publications/{id}/commentaires`
- `POST /api/publications/{id}/commentaires`
- `DELETE /api/publications/{pubId}/commentaires/{id}`

### Connexions
- `POST /api/connexions/{destinataireId}` — Envoyer demande
- `PUT /api/connexions/{id}/accept` — Accepter
- `PUT /api/connexions/{id}/reject` — Refuser
- `GET /api/connexions/received` — Demandes reçues
- `GET /api/connexions/sent` — Demandes envoyées
- `GET /api/connexions` — Mes connexions

### Messages (temps réel via WebSocket)
- `POST /api/messages/{destinataireId}` — Envoyer
- `GET /api/messages/conversation/{otherUserId}` — Conversation
- `GET /api/messages/unread` — Non lus
- `PUT /api/messages/{id}/read` — Marquer lu
- `PUT /api/messages/conversation/{otherUserId}/read` — Marquer conversation lue
- WebSocket: `ws://localhost:8080/ws` (STOMP + SockJS)

### Notifications (temps réel via WebSocket)
- `GET /api/notifications` — Toutes
- `GET /api/notifications/unread-count` — Nombre non lues
- `PUT /api/notifications/{id}/read` — Marquer lue
- `PUT /api/notifications/read-all` — Tout marquer lu

### Étudiant (ROLE_ETUDIANT)
- `GET/POST/DELETE /api/etudiant/competences`
- `GET/POST/DELETE /api/etudiant/formations`
- `GET/POST/DELETE /api/etudiant/projets`
- `GET/POST/DELETE /api/etudiant/experiences`

### Enseignant (ROLE_ENSEIGNANT)
- `GET/POST/DELETE /api/enseignant/experiences`

### Chercheur (ROLE_CHERCHEUR)
- `GET/POST/DELETE /api/chercheur/recherches`

### Admin (ROLE_ADMIN)
- `GET /api/admin/users/pending` — Inscriptions en attente
- `GET /api/admin/users` — Tous les utilisateurs
- `PUT /api/admin/users/{id}/activate` — Valider inscription
- `DELETE /api/admin/users/{id}` — Supprimer utilisateur
- `DELETE /api/admin/publications/{id}` — Supprimer publication

## Sécurité
- JWT Bearer Token (header `Authorization: Bearer <token>`)
- BCrypt pour les mots de passe
- Accès contrôlé par rôle via `@PreAuthorize`
- Inscription validée par admin avant activation
- CORS configuré pour localhost:3000 et localhost:5173

## WebSocket (Messagerie temps réel)
- Endpoint: `/ws` (SockJS)
- Subscribe: `/user/{userId}/queue/messages` et `/user/{userId}/queue/notifications`
- Send: `/app/chat.send` avec payload `{expediteurId, destinataireId, contenu, sujet?}`
