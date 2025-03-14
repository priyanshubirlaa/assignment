# Blog API Documentation

## Overview

This API allows users to create, read, update, and delete blog posts. It also provides functionality to generate summaries using Gemini AI.

##  

### Blog Operations

#### Get all blogs (paginated)

**Endpoint:** `GET /api/blogs`
**Description:** Fetches a paginated list of blogs.
**Query Parameters:**

- `page` (default: `0`)
- `size` (default: `5`)

**Authentication:** Requires Bearer Token

#### Get a blog by ID

**Endpoint:** `GET /api/blogs/{id}`
**Description:** Fetches a single blog post by its ID.

**Authentication:** Requires Bearer Token

#### Create a new blog

**Endpoint:** `POST /api/blogs`
**Description:** Creates a new blog post.
**Request Body:**

```json
{
  "title": "Blog Title",
  "content": "Blog Content",
  "author": "Author Name"
}
```

**Authentication:** Requires Bearer Token

#### Update a blog

**Endpoint:** `PUT /api/blogs/{id}`
**Description:** Updates an existing blog post.
**Request Body:**

```json
{
  "title": "Updated Blog Title",
  "content": "Updated Blog Content",
  "author": "Updated Author Name"
}
```

**Authentication:** Requires Bearer Token

#### Delete a blog

**Endpoint:** `DELETE /api/blogs/{id}`
**Description:** Deletes a blog post by its ID.

**Authentication:** Requires Bearer Token

#### Generate summary using Gemini AI

**Endpoint:** `GET /api/blogs/{id}/summary`
**Description:** Generates a summary of a blog post using Gemini AI.

**Authentication:** Requires Bearer Token

## Post Blog API

#### Post a new blog

**Endpoint:** `POST /api/blogs`
**Description:** Posts a new blog entry.
**Request Body:**

```json
{
  "title": "New Blog Post",
  "content": "This is the content of the blog post.",
  "author": "Author Name"
}
```

**Response:**

```json
{
  "id": 1,
  "title": "New Blog Post",
  "content": "This is the content of the blog post.",
  "author": "Author Name",
  "createdAt": "2024-03-14T12:00:00"
}
```

**Authentication:** Requires Bearer Token

## API Documentation (Swagger)

Swagger is integrated for API documentation and testing. **No authentication is required** to access the Swagger UI.

**Swagger UI URL:** [/swagger-ui/index.html](/swagger-ui/index.html)

## JWT Authentication

All API endpoints **(except Swagger and token generation)** require a **Bearer Token**, which expires after **1 hour** and needs to be regenerated.

#### Generate JWT Token

**Endpoint:** `GET /api/blogs/generate-token`
**Description:** Generates a JWT token for authentication.

**Authentication:** No authentication required

## Database Configuration

This project uses MySQL hosted on **Railway**. Below are the database connection details:

**Database Connection:**

```
spring.datasource.url=jdbc:mysql://root:dPdXjZRAutdqNBLzvAmyZPMLaarNaFLm@tramway.proxy.rlwy.net:55144/railway
spring.datasource.username=root
spring.datasource.password=dPdXjZRAutdqNBLzvAmyZPMLaarNaFLm
```

**JPA Configuration:**

```
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## Cache Configuration (Redis)

The application uses Redis for caching to improve performance and reduce database load.

```
spring.redis.host=redis-service
spring.redis.port=6379
spring.redis.password=1lqRL8GTps0PMO4a2Ek4MjU5ZHggQZfg
```

## AI Integration (Gemini API)

The API integrates Gemini AI for generating blog summaries.

```
gemini.api.key=AIzaSyALUnjKTRzrctqD8wOQwccHdIK******
```

## Entity: Blog

The `Blog` entity represents a blog post.

### Attributes:

- `id` (Long, Auto-generated, Primary Key)
- `title` (String, Required)
- `content` (String, Stored as LONGTEXT)
- `author` (String, Required)
- `createdAt` (LocalDateTime, Auto-generated, Not updatable)

### Table Structure (blogs):

| Column    | Type     | Constraints                 |
| --------- | -------- | --------------------------- |
| id        | BIGINT   | Primary Key, Auto-increment |
| title     | VARCHAR  | Not Null                    |
| content   | LONGTEXT |                             |
| author    | VARCHAR  | Not Null                    |
| createdAt | DATETIME | Not Null, Not Updatable     |

## Security Configuration

Spring Security's default authentication is disabled.

## Deployment & Infrastructure

### Building Docker Image

To build the Docker image, run:

```sh
docker build -t priyanshubirla/myapp:latest .
```

Push the image to Docker Hub:

```sh
docker push priyanshubirla/myapp:latest
```

### Kubernetes Deployment

- **Dockerized:** The application has been containerized using Docker.
- **Kubernetes Deployment:** The application is deployed on Kubernetes using `deployment.yaml` and `service.yaml` for scalability and orchestration.
- **Swagger API Documentation:** Swagger is integrated for API documentation and testing. No authentication is required for accessing API documentation.
- **Logging:** SLF4J is used for structured logging to track application events and debugging.
- **Redis Caching:** Used to optimize response times and reduce unnecessary database queries.

#### 1️⃣ Apply Deployment and Service

```sh
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

#### 2️⃣ Verify Deployment

```sh
kubectl get pods
kubectl get deployments
kubectl get services
```

#### 3️⃣ Check Logs

```sh
kubectl logs -f <pod-name>
```

#### 4️⃣ Expose Service (If Needed)

```sh
kubectl port-forward svc/<service-name> 8081:8080
```

#### 5️⃣ Delete Deployment (If Required)

```sh
kubectl delete -f deployment.yaml
kubectl delete -f service.yaml
```

### **Docker Image**

Your application is built and pushed to Docker Hub with:

```yaml
image: priyanshubirla/myapp:latest
```

---

This API provides full CRUD functionality for blog management, integrates AI for generating summaries, and is deployed on Kubernetes with caching and logging support. 🚀

