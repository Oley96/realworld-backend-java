# Realworld API Backend

Spring Boot implementation of the [Realworld](https://realworld.io/) Conduit specification - a full-featured REST API for a blogging/social platform.

## Features

- **User Management**: Registration, authentication, profile updates
- **Articles**: Create, read, update, delete, list with filtering
- **Comments**: Add and remove comments on articles
- **Tags**: Article tag management
- **Social**: Follow/unfollow users, favorite/unfavorite articles
- **Authentication**: JWT-based token authentication

## Tech Stack

- **Java 21** with Spring Boot 3.3.5
- **PostgreSQL** database with **Liquibase** migrations
- **JWT** authentication (jjwt library)
- **JPA/Hibernate** for data persistence
- **Lombok** for reducing boilerplate
- **Gradle** for build automation

## Getting Started

### Prerequisites

- Java 21
- Docker and Docker Compose (for containerized deployment)

### Local Development

```bash
# Build the project
./gradlew clean build

# Run tests
./gradlew test

# Run the application
./gradlew bootRun
```

The application will be available at `http://localhost:1748`

### Docker Compose

```bash
docker-compose up -d
```

This starts:
- **Backend API**: `http://localhost:1748`
- **PostgreSQL Database**: `localhost:5437`

### Environment Configuration

Application profiles:
- `local`: Local development environment
- `stage`: Staging environment


## Project Structure

```
src/main/java/app/demo/realworld/
├── config/           # Security, persistence configuration
├── controller/       # REST API endpoints
├── exception/        # Custom exception handlers
├── model/            # DTOs, requests, responses, database entities
├── repository/       # JPA repositories
├── security/         # JWT authentication, user details service
├── service/          # Business logic layer
├── utils/            # Utility classes (SlugUtil)
└── validation/       # Custom validators
```

### Key Components

- **Controllers**: `ArticleController`, `UserController`, `ProfileController`, `CommentController`, `TagController`
- **Services**: Business logic for articles, users, comments, tags, followers, favorites
- **Security**: JWT token provider, authentication filter, user details service
- **Database**: JPA entities with Liquibase changelogs in `src/main/resources/db/changelog/`

## Testing

Run the full test suite:

```bash
./gradlew test
```

Test results are reported in JUnit XML format. The CI/CD pipeline automatically runs tests on pull requests and reports results.

## API Endpoints

The API implements the Realworld specification including:

- `POST /api/users/login` - User authentication
- `GET /api/articles` - List articles (with filters: tag, author, favorited)
- `POST /api/articles` - Create article
- `GET /api/articles/{slug}` - Get article
- `POST /api/articles/{slug}/comments` - Add comment
- `POST /api/profiles/{username}/follow` - Follow user
- And more...

See the [Realworld spec](https://realworld.io/docs/specs/backend-specs/endpoints) for complete API documentation.

## CI/CD

- **GitHub Actions** workflow runs on pull requests
- Automated testing with Gradle
- Test reports posted as PR comments
- Deployment workflow configured for staging/production

## License

This project follows the Realworld specification implementation guidelines.
