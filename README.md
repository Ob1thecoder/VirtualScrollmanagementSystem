# Virtual Scroll Management System


## 📜 Project Overview

The Virtual Scroll Management System is a comprehensive full-stack web application designed as a digital library for managing and sharing text-based documents called "scrolls". This educational project demonstrates modern web development practices using a microservices architecture with Django (Python) for the frontend and Spring Boot (Java) for the backend API.

### 🎯 Key Features

- **User Authentication & Authorization**: Secure user registration, login, and role-based access control
- **Document Management**: Upload, view, download, and manage digital scrolls
- **Social Features**: Like/unlike scrolls, view popular content
- **Admin Panel**: User management, content moderation, and system administration
- **Guest Access**: Limited browsing capabilities for non-registered users
- **Responsive Design**: Mobile-friendly interface built with modern web standards

## 🏗️ Architecture

This application follows a microservices architecture pattern:

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│    Nginx        │    │     Django       │    │   Spring Boot   │
│  (Port 80)      │ -->│   (Port 8000)    │ -->│   (Port 8081)   │
│  Load Balancer  │    │   Frontend/UI    │    │   REST API      │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │                        │
                                ▼                        ▼
                        ┌─────────────────────────────────────┐
                        │         PostgreSQL Database        │
                        │            (Port 5432)             │
                        └─────────────────────────────────────┘
```

### Component Breakdown

- **Frontend (Django)**: Handles user interface, authentication, and user experience
- **Backend API (Spring Boot)**: Manages business logic, data processing, and scroll operations
- **Database (PostgreSQL)**: Stores user data, scroll metadata, and application state
- **Nginx**: Reverse proxy for load balancing and routing requests
- **Docker**: Containerization for consistent deployment across environments

## 🚀 Quick Start

### Prerequisites

- **Docker & Docker Compose** (recommended)
- **Python 3.9+** (for local development)
- **Java 11+** (for local development)
- **PostgreSQL** (for local development)

### Docker Deployment 

1. **Clone the repository**
   ```bash
   git clone https://github.com/Ob1thecoder/VirtualScrollmanagementSystem.git
   cd VirtualScrollmanagementSystem
   ```

2. **Start all services**
   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - Main application: http://localhost
   - Django admin: http://localhost/admin
   - API documentation: http://localhost/api



## Tech Stack

### Frontend
- **Django 5.2.4**: Web framework for rapid development
- **Django REST Framework**: API development and integration
- **HTML/CSS/JavaScript**: Frontend technologies
- **Bootstrap**: Responsive UI components
- **Pillow**: Image processing for user profiles

### Backend
- **Spring Boot**: Java-based REST API framework
- **Spring Data JPA**: Data persistence and ORM
- **Spring Security**: Authentication and authorization
- **Maven/Gradle**: Dependency management

### Database & Infrastructure
- **PostgreSQL**: Primary database for production
- **SQLite**: Development database
- **Docker**: Containerization
- **Nginx**: Reverse proxy and load balancer
- **AWS EC2**: Cloud hosting platform

### Development Tools
- **Git**: Version control
- **VS Code**: Recommended IDE
- **Postman**: API testing











### Authentication Endpoints
- `POST /api/login/` - User login
- `POST /api/register/` - User registration
- `POST /api/logout/` - User logout

### Scroll Management
- `GET /api/scrolls/` - List all scrolls
- `POST /api/scrolls/upload/` - Upload new scroll
- `GET /api/scrolls/{id}/` - Get scroll details
- `DELETE /api/scrolls/{id}/` - Delete scroll
- `POST /api/scrolls/{id}/like/` - Like/unlike scroll

### User Management
- `GET /api/users/` - List users (admin only)
- `POST /api/users/ban/{id}/` - Ban user (admin only)
- `POST /api/users/unban/{id}/` - Unban user (admin only)



## Deployment

```bash
# Build and deploy
docker-compose up --build -d

# View logs
docker-compose logs -f


```



- **Developer** - [@Ob1thecoder](https://github.com/Ob1thecoder)



