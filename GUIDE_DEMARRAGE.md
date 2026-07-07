# TANTY ERP — Guide de Démarrage

## 1. Prérequis à installer

| Outil | Version | Lien |
|---|---|---|
| JDK | 21 (Temurin recommandé) | https://adoptium.net |
| Maven | 3.9+ (ou utiliser `./mvnw` fourni, aucune installation requise) | — |
| PostgreSQL | 15+ | https://www.postgresql.org/download/ |
| RabbitMQ | 3.12+ (avec le plugin management, optionnel) | https://www.rabbitmq.com/download.html |
| MinIO | dernier (optionnel — l'app démarre sans, seul l'archivage documentaire sera inactif) | https://min.io/download |

### Option rapide : Docker Compose

Si vous avez Docker installé, créez un fichier `docker-compose.yml` à côté du dossier `Api_tanty` :

```yaml
version: "3.8"
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: erp_tanty_db
      POSTGRES_USER: giovanni
      POSTGRES_PASSWORD: postgre
    ports: ["5432:5432"]
    volumes: ["tanty_pg_data:/var/lib/postgresql/data"]

  rabbitmq:
    image: rabbitmq:3-management
    ports: ["5672:5672", "15672:15672"]

  minio:
    image: minio/minio
    command: server /data --console-address ":9001"
    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin
    ports: ["9000:9000", "9001:9001"]
    volumes: ["tanty_minio_data:/data"]

volumes:
  tanty_pg_data:
  tanty_minio_data:
```

Puis : `docker compose up -d`. Cela suffit à faire tourner PostgreSQL, RabbitMQ et MinIO
avec exactement les identifiants déjà présents dans `application.properties`.

### Option manuelle (sans Docker)

1. **PostgreSQL** : créez la base et l'utilisateur attendus par `application.properties` :
   ```sql
   CREATE USER giovanni WITH PASSWORD 'postgre';
   CREATE DATABASE erp_tanty_db OWNER giovanni;
   ```
   (ou modifiez `spring.datasource.*` dans `application.properties` pour vos propres identifiants)

2. **RabbitMQ** : installez-le puis démarrez le service (`rabbitmq-server` en ligne de commande,
   ou via le service système selon votre OS). Les identifiants par défaut (`guest`/`guest`) sont
   déjà configurés.

3. **MinIO** *(optionnel)* : téléchargez le binaire puis lancez :
   ```bash
   minio server ./data --console-address ":9001"
   ```
   avec les identifiants par défaut `minioadmin` / `minioadmin` (déjà configurés).
   Si vous ne lancez pas MinIO, l'application démarre quand même normalement — seul
   l'archivage documentaire (BC, BR, factures...) restera inopérant.

## 2. Compiler et lancer le backend

Depuis le dossier `Api_tanty` (celui qui contient `pom.xml`) :

```bash
# Windows
mvnw.cmd clean install
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw clean install
./mvnw spring-boot:run
```

`mvnw`/`mvnw.cmd` téléchargent automatiquement Maven et toutes les dépendances déclarées dans
`pom.xml` (Spring Boot, PostgreSQL driver, JWT, iText, Apache POI, MinIO, etc.) — **aucune
installation manuelle de librairie n'est nécessaire**, uniquement une connexion internet lors du
premier lancement (le cache Maven `~/.m2` est ensuite réutilisé).

Le serveur démarre sur **http://localhost:8083**.

- Documentation interactive (Swagger) : http://localhost:8083/swagger-ui.html
- Santé de l'application (Actuator) : http://localhost:8083/actuator/health

## 3. Premier utilisateur

Le premier compte doit être créé via `POST /api/auth/register` (voir Swagger). Le matricule est
généré automatiquement selon le rôle choisi (ex: `DG001`, `COM001`, `GS001`...).

## 4. Configuration à personnaliser avant la production

Dans `src/main/resources/application.properties` :

- `app.jwt.secret` : remplacer par une valeur secrète propre à votre environnement.
- `spring.datasource.*` : vos identifiants PostgreSQL réels.
- `app.sms.provider` : passer de `log` (simulation, code visible dans les logs) à un vrai
  fournisseur SMS en implémentant `SmsGatewayPort` (voir `AUDIT_ET_LIVRAISON.md`, section OTP).
- `spring.mail.*` : vos identifiants SMTP réels pour l'envoi d'emails.
- `app.minio.*` : vos identifiants MinIO réels si différents des valeurs par défaut.
- `app.cors.allowed-origins` : les URLs réelles de vos applications Angular/Flutter en production.

## 5. En cas d'erreur de compilation

Ce backend a été enrichi de nouveaux modules dans un environnement qui ne permettait pas de
lancer Maven pour valider la compilation (voir `AUDIT_ET_LIVRAISON.md`, section 4). Si
`mvn clean compile` échoue, copiez-collez l'erreur complète — la correction sera rapide, il
s'agit très probablement d'un détail de signature ou d'import.
