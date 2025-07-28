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

### Option 1: Docker Deployment (Recommended)

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

### Option 2: Local Development

1. **Set up Django (Frontend)**
   ```bash
   cd VirtualScrollSys/auth
   pip install -r requirement.txt
   python manage.py migrate
   python manage.py runserver 8000
   ```

2. **Set up Spring Boot (Backend)**
   ```bash
   cd VirtualScrollSys/app
   ./gradlew bootRun
   ```

3. **Set up Database**
   ```bash
   # Install PostgreSQL and create database
   createdb virtualscroll
   ```

### Option 3: Using Makefile

```bash
# Start all services
make run-all

# Start only Django
make run-django

# Start only Spring Boot
make run-springboot

# Stop all services
make clean
```

## 🛠️ Technology Stack

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

## 📂 Project Structure

```
VirtualScrollmanagementSystem/
├── docker-compose.yml          # Multi-container Docker configuration
├── nginx.conf                  # Nginx reverse proxy configuration
├── Makefile                   # Build and deployment automation
├── README.md                  # Project documentation
│
├── VirtualScrollSys/
│   ├── auth/                  # Django Frontend Application
│   │   ├── manage.py          # Django management script
│   │   ├── requirement.txt    # Python dependencies
│   │   ├── Dockerfile         # Django container configuration
│   │   ├── auth/              # Django project settings
│   │   │   ├── settings.py    # Application configuration
│   │   │   ├── urls.py        # URL routing
│   │   │   └── wsgi.py        # WSGI configuration
│   │   └── authapp/           # Main Django application
│   │       ├── models.py      # Database models
│   │       ├── views.py       # Application logic
│   │       ├── forms.py       # Web forms
│   │       ├── urls.py        # App-specific URLs
│   │       └── templates/     # HTML templates
│   │
│   └── app/                   # Spring Boot Backend API
│       ├── build.gradle       # Gradle build configuration
│       ├── Dockerfile         # Spring Boot container
│       └── src/               # Java source code
│           └── main/
│               ├── java/      # Application logic
│               └── resources/ # Configuration files
```

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the root directory:

```env
# Database Configuration
DATABASE_URL=postgres://postgres:password@db:5432/virtualscroll
POSTGRES_DB=virtualscroll
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password

# Django Configuration
DJANGO_SECRET_KEY=your-secret-key-here
DJANGO_DEBUG=False
DJANGO_ALLOWED_HOSTS=localhost,127.0.0.1,your-domain.com

# Spring Boot Configuration
SPRING_BOOT_API_URL=http://springboot-service:8081/api
```

### Database Setup

The application supports both SQLite (development) and PostgreSQL (production):

```bash
# Run migrations
python manage.py migrate

# Create superuser
python manage.py createsuperuser

# Load sample data (optional)
python manage.py loaddata sample_data.json
```

## 🧪 Testing

```bash
# Run Django tests
cd VirtualScrollSys/auth
python manage.py test

# Run Spring Boot tests
cd VirtualScrollSys/app
./gradlew test
```

## 📖 API Documentation

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

## 🚀 Deployment

### Docker Deployment

```bash
# Build and deploy
docker-compose up --build -d

# View logs
docker-compose logs -f

# Scale services
docker-compose up --scale django=2 --scale springboot=2
```

### AWS Deployment

The application is configured for AWS Elastic Beanstalk deployment using `Dockerrun.aws.json`.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📋 Roadmap

- [ ] Real-time notifications
- [ ] Advanced search and filtering
- [ ] File format support (PDF, DOCX)
- [ ] Social sharing features
- [ ] Mobile application
- [ ] Advanced analytics dashboard
- [ ] Multi-language support

## 🐛 Known Issues

- File upload size limited to 100MB
- Guest users have limited functionality
- Search functionality is basic (no advanced filters)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Developer** - [@Ob1thecoder](https://github.com/Ob1thecoder)

## 🙏 Acknowledgments

- Django and Spring Boot communities for excellent documentation
- AWS for cloud hosting capabilities
- All contributors and testers

---

**Note**: This is an educational project designed to demonstrate full-stack development principles and is not intended for production use without proper security auditing and performance optimization.