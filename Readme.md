# GitHub Repository Searcher

A RESTful backend application built using **Java, Spring Boot, Spring Data JPA, and PostgreSQL**. It integrates with the GitHub REST API to search public repositories, persist repository information, and provide filtering and sorting capabilities.

## Features

- Search public GitHub repositories by keyword and programming language.
- Sort GitHub search results by stars, forks, or last updated date.
- Store repository information in PostgreSQL.
- Update existing repository records using their GitHub IDs to avoid duplicates.
- Retrieve saved repositories with optional language and minimum-star filters.
- Sort stored repositories by stars, forks, or last updated date.
- Validate incoming API requests using Jakarta Bean Validation.
- Handle invalid requests and GitHub API failures.
- Automated unit and web-layer tests using JUnit 5, Mockito, and MockMvc.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming |
| Spring Boot 4 | REST API framework |
| Spring Data JPA / Hibernate | Database persistence |
| PostgreSQL (Neon) | Relational database |
| Spring RestClient | GitHub API integration |
| Jakarta Bean Validation | Request validation |
| JUnit 5, Mockito, MockMvc | Automated testing |
| Maven | Build and dependency management |

## API Endpoints

### 1. Search GitHub Repositories

**POST** `/api/github/search`

Fetches repositories from GitHub and saves or updates them in PostgreSQL.

**Request body**

```json
{
  "query": "spring boot",
  "language": "Java",
  "sort": "stars"
}
```

**Example response**

```json
{
  "message": "Repositories fetched and saved successfully",
  "repositories": [
    {
      "id": 12345,
      "name": "spring-boot-example",
      "description": "Example Spring Boot project",
      "owner": "developer",
      "language": "Java",
      "stars": 1500,
      "forks": 200,
      "lastUpdated": "2026-11-13T10:30:00Z"
    }
  ]
}
```

The example response contains illustrative repository data.

### 2. Retrieve Saved Repositories

**GET** `/api/github/repositories`

Returns repositories stored in PostgreSQL.

**Optional query parameters**

| Parameter | Description |
|---|---|
| `language` | Filter by programming language |
| `minStars` | Minimum number of stars |
| `sort` | `stars`, `forks`, or `updated` |

**Example requests**

```http
GET /api/github/repositories
GET /api/github/repositories?language=Java
GET /api/github/repositories?minStars=100
GET /api/github/repositories?language=Java&minStars=100&sort=forks
```

Results are sorted by stars in descending order when no sort parameter is provided.

## Getting Started

### Prerequisites

- JDK compatible with the project's Maven configuration
- PostgreSQL database (Neon or local PostgreSQL)
- Git

### 1. Clone the Repository

```bash
git clone <repository-url>
cd <repository-directory>
```

Replace the placeholders with the actual GitHub repository URL and directory.

### 2. Configure Environment Variables

Create a `.env` file in the project root:

```dotenv
DATABASE_URL=jdbc:postgresql://YOUR_NEON_HOST/neondb?sslmode=require
DATABASE_USERNAME=YOUR_NEON_USERNAME
DATABASE_PASSWORD=YOUR_NEON_PASSWORD
```

Configure these variables in your IDE run configuration or export them into your terminal environment before launching the application.

**Note:** Spring Boot does not automatically load `.env` files. Never commit real credentials.

### 3. Run the Application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

The application runs at:

`http://localhost:8080`

### 4. Run Automated Tests

On Windows:

```powershell
.\mvnw.cmd clean test
```

On Linux or macOS:

```bash
./mvnw clean test
```

The test suite covers service filtering, sorting, invalid sorting, controller responses, and request validation.

## Error Handling

The application provides structured error responses for invalid requests.

**Example — Invalid sorting parameter**

```json
{
  "error": "Bad Request",
  "message": "Invalid sort value. Use stars, forks, or updated."
}
```

The application also handles GitHub API request failures, including rate-limit-related errors and upstream service failures.

## Project Architecture

The application follows a layered architecture:

- **Controller:** Defines REST endpoints and HTTP responses.
- **Service:** Implements business logic and GitHub API integration.
- **Repository:** Handles PostgreSQL persistence.
- **Entity:** Represents stored repository data.
- **DTO:** Defines request, external API, and response data structures.
- **Mapper:** Converts between GitHub API DTOs, entities, and application response DTOs.
- **Exception Handler:** Provides centralized handling of invalid requests.

## Testing

The project includes automated tests using JUnit 5, Mockito, and MockMvc.

Tests verify repository sorting, filtering, invalid sorting behavior, POST response construction, and rejection of invalid search requests.

## Security

Database credentials are supplied through environment variables rather than committed to source control. The `.env` file is excluded from Git tracking.

## Author

Developed as a Java Spring Boot backend assignment.