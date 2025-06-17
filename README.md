# Social Media Application

A full-featured social media platform built with Spring Boot, Hibernate, and PostgreSQL.


## Features

- **User Authentication**: Secure sign-up and login using JWT tokens
- **User Profiles**: User information and profile management
- **Posts**: Create, read, update, and delete posts
- **Comments**: Interact with posts through comments
- **Likes**: Like/unlike posts and comments
- **Views**: Track post views
- **Subscriptions**: Follow other users and view their content in your feed
- **API Documentation**: Comprehensive Swagger documentation

## Technologies Used

- **Backend**: Spring Boot, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **ORM**: Hibernate
- **Authentication**: JWT (JSON Web Tokens)
- **API Documentation**: Swagger/Springfox

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- PostgreSQL

### Database Setup

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE socialmedia;
   ```

2. Configure the database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/socialmedia
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. The application will be available at `http://localhost:8080`

## API Documentation

Once the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui/
```

## API Endpoints and Request/Response Objects

### Authentication

#### Sign Up
```http
POST /api/auth/signup
```
Request body:
```json
{
  "username": "string",
  "email": "user@example.com",
  "password": "string"
}
```
Response:
```json
{
  "message": "User registered successfully!"
}
```

#### Sign In
```http
POST /api/auth/signin
```
Request body:
```json
{
  "username": "string",
  "password": "string"
}
```
Response:
```json
{
  "token": "string",
  "type": "Bearer",
  "id": 0,
  "username": "string",
  "email": "string",
  "roles": ["ROLE_USER"]
}
```

### Users

#### Update User Profile
```http
PUT /api/users/{id}
```
Request body:
```json
{
  "bio": "string",
  "profilePicture": "string"
}
```

#### Subscribe to User
```http
POST /api/users/{subscriberId}/subscribe/{targetId}
```
Response:
```json
{
  "message": "Subscribed successfully"
}
```

### Posts

#### Create Post
```http
POST /api/posts/user/{userId}
```
Request body:
```json
{
  "title": "string",
  "content": "string"
}
```
Response:
```json
{
  "id": 0,
  "title": "string",
  "content": "string",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z",
  "viewCount": 0,
  "likes": []
}
```

#### Like Post
```http
POST /api/posts/{postId}/like/{userId}
```
Response:
```json
{
  "message": "Post liked successfully"
}
```

### Comments

#### Create Comment
```http
POST /api/comments/post/{postId}/user/{userId}
```
Request body:
```json
{
  "content": "string"
}
```
Response:
```json
{
  "id": 0,
  "content": "string",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z",
  "likes": []
}
```

#### Like Comment
```http
POST /api/comments/{commentId}/like/{userId}
```
Response:
```json
{
  "message": "Comment liked successfully"
}
```

## Database Schema

### User
```sql
{
  "id": "bigint (auto-generated)",
  "username": "string (unique)",
  "email": "string (unique)",
  "password": "string (encrypted)",
  "bio": "string",
  "profilePicture": "string",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Post
```sql
{
  "id": "bigint (auto-generated)",
  "title": "string",
  "content": "text",
  "userId": "bigint (foreign key)",
  "viewCount": "bigint",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Comment
```sql
{
  "id": "bigint (auto-generated)",
  "content": "text",
  "postId": "bigint (foreign key)",
  "userId": "bigint (foreign key)",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### Like
```sql
{
  "id": "bigint (auto-generated)",
  "userId": "bigint (foreign key)",
  "postId/commentId": "bigint (foreign key)",
  "createdAt": "timestamp"
}
```

### Subscription
```sql
{
  "subscriberId": "bigint (foreign key)",
  "targetId": "bigint (foreign key)",
  "createdAt": "timestamp"
}
```

## Security

The application uses Spring Security and JWT for authentication and authorization. The security configuration includes:

- JWT-based authentication
- Role-based access control
- Secure password hashing with BCrypt
- CORS configuration

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
