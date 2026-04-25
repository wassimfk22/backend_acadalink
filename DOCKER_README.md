# AcadLink Backend — Docker

## Build de l'image
```bash
docker build -t acadlink-backend:1.0.0 .
```

## Lancer (backend seul, BDD externe)
```bash
docker run -d --name acadlink-backend -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/Acadlink \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=Support@2525 \
  acadlink-backend:1.0.0
```

## Lancer avec PostgreSQL (recommandé)
```bash
docker compose up -d --build
```
API disponible sur http://localhost:8080

## Pousser sur Docker Hub
```bash
docker tag acadlink-backend:1.0.0 <user>/acadlink-backend:1.0.0
docker push <user>/acadlink-backend:1.0.0
```
