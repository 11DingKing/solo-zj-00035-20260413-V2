![CI/CD Pipeline](https://github.com/jmoreno-dev/product-manager/actions/workflows/main.yml/badge.svg?branch=main&v=1)

# Product Manager

A full-stack CRUD application for managing products built with **Angular** (frontend) and **Spring Boot** (backend).

[![Angular](https://img.shields.io/badge/-Angular-%23DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.io/)
[![Java](https://img.shields.io/badge/-Java-%23007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-%236DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Swagger](https://img.shields.io/badge/-Swagger-%2385EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://product-manager-5xaf.onrender.com/swagger-ui/index.html)
[![Docker](https://img.shields.io/badge/-Docker-%232496ED?style=for-the-badge&logo=docker&logoColor=white)](#)

## 🌍 Live API Documentation
The backend of this project is fully containerized and deployed. You can explore, test, and interact with the API endpoints in real-time through the Swagger UI:

🔗 **[Click here to open Swagger UI](https://product-manager-5xaf.onrender.com/swagger-ui/index.html)**

> **Note:** Since this is hosted on a free tier, the server may need about 30 seconds to "spin up" on the first request.

---

## 🎯 Overview

This project demonstrates a clean implementation of a product management system with:
- A lightweight REST API backend using Spring Boot
- A responsive Angular frontend with signal-based state management
- Basic CRUD operations (Create, Read, Update, Delete)
- Simple and intuitive user interface
- MySQL database integration

## 🛠️ Technology Stack


### Backend
- **Java 17** - Programming language
- **Spring Boot 3.5** - REST API framework
- **Gradle 8.x** - Build tool
- **Spring Data JPA** - Data persistence
- **Lombok** - Code generation
- **MySQL 8.x** - Database

### Frontend
- **Angular 21.0.0** - Frontend framework
- **TypeScript 5.9.2** - Programming language
- **SCSS** - Styling
- **RxJS 7.8.0** - Reactive programming
- **npm 10.9.3** - Package manager

## 📋 Prerequisites

Before running this project, ensure you have installed:
- **Java Development Kit (JDK) 17+**
- **Node.js 18+** and npm 10.9.3+
- **MySQL 8.x** - Database server
- **Git**
- Your preferred code editor (VS Code, IntelliJ IDEA, etc.)

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/jmoreno-dev/product-manager.git
cd product-manager
```

### Backend Setup

1. **Configure MySQL Database:**
   - Create a database named `product_manager`
   - Update `backend/src/main/resources/application.properties` with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/product_manager
     spring.datasource.username=root
     spring.datasource.password=your_password
     ```

2. Navigate to the backend directory:
   ```bash
   cd backend
   ```

3. Build the project:
   ```bash
   ./gradlew build
   ```
   (On Windows, use `gradlew.bat build`)

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The API will be available at `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   ng serve
   ```

The application will be available at `http://localhost:4200`

## 📁 Project Structure

```
product-manager/
├── backend/                                    # Spring Boot backend
│   ├── src/main/java/com/jmoreno_dev/productmanager/
│   │   ├── ProductmanagerApplication.java     # Main Spring Boot application
│   │   ├── Config/
│   │   │   └── CorsConfig.java               # CORS configuration
│   │   ├── controller/
│   │   │   └── ProductController.java        # REST endpoints
│   │   ├── entity/
│   │   │   └── Product.java                  # Product entity/model
│   │   ├── repository/
│   │   │   └── ProductRepository.java        # JPA repository
│   │   └── service/
│   │       └── ProductService.java           # Business logic
│   ├── src/main/resources/
│   │   └── application.properties            # Database & server config
│   ├── build.gradle                          # Gradle build file
│   ├── gradlew & gradlew.bat                 # Gradle wrapper scripts
│   |── settings.gradle                       # Gradle settings
│   └── Dockerfile                           # Dockerfile for backend
│
└── frontend/                                  # Angular frontend
    ├── src/
    │   ├── app/
    │   │   ├── app.ts                       # Root component
    │   │   ├── app.html                     # Root template
    │   │   ├── app.routes.ts                # Route configuration
    │   │   ├── app.config.ts                # App configuration
    │   │   ├── components/
    │   │   │   ├── product-list.component.ts
    │   │   │   ├── product-list.component.html
    │   │   │   └── product-list.component.scss
    │   │   ├── models/
    │   │   │   └── product.model.ts         # Product interface
    │   │   └── services/
    │   │       └── product.service.ts       # HTTP service
    │   ├── main.ts                          # Application bootstrap
    │   ├── index.html                       # HTML entry point
    │   └── styles.scss                      # Global styles
    ├── package.json                         # npm dependencies
    ├── angular.json                         # Angular configuration
    ├── tsconfig.json                        # TypeScript configuration
    └── karma.conf.js                        # Testing configuration
```

## ✨ Features

### CRUD Operations
- **Create** - Add new products
- **Read** - View product list and details
- **Update** - Modify existing products
- **Delete** - Remove products

### Frontend Highlights
- **Signal-Based State Management** - Modern Angular reactive patterns
- **Responsive UI** - Clean and intuitive interface
- **Component Architecture** - Well-organized Angular components

## 📝 API Endpoints

All endpoints are prefixed with `http://localhost:8080/api/products`

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|---------------|----------|
| GET | `/api/products` | Get all products | — | Array of products |
| GET | `/api/products/{id}` | Get a product by ID | — | Product object |
| POST | `/api/products` | Create a new product | Product object | Created product with ID |
| PUT | `/api/products/{id}` | Update a product | Product object | Updated product |
| DELETE | `/api/products/{id}` | Delete a product | — | 204 No Content |

### Product Object Structure
```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Product description",
  "price": 99.99,
  "quantity": 10
}
```

### Example Requests

**Get all products:**
```bash
curl http://localhost:8080/api/products
```

**Get product by ID:**
```bash
curl http://localhost:8080/api/products/1
```

**Create product:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","description":"High-end laptop","price":1299.99,"quantity":5}'
```

**Update product:**
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Laptop","description":"Updated description","price":1199.99,"quantity":3}'
```

**Delete product:**
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

## 🐳 Docker Setup (Optional)

1. Create `docker-compose.yml` in the project root:

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: product_manager
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/product_manager
    depends_on:
      - mysql

  frontend:
    build: ./frontend
    ports:
      - "4200:4200"
    depends_on:
      - backend

volumes:
  mysql_data:
```

2. Run all services:
```bash
docker-compose up --build
```

## ⚙️ CORS Configuration

The backend is configured to accept requests from the Angular frontend running on `localhost:4200`. The CORS configuration is defined in:
- **File**: `backend/src/main/java/com/jmoreno_dev/productmanager/Config/CorsConfig.java`
- **Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS
- **Allowed Origins**: http://localhost:4200

## 🔄 Workflow

1. **Frontend** - User interacts with the Angular interface
2. **Signals** - State updates trigger reactive changes
3. **HTTP Request** - Angular sends requests to the backend API
4. **Backend Processing** - Spring Boot processes and persists data
5. **Response** - API returns data to the frontend
6. **UI Update** - Angular signals update the display

## 🧪 Testing

(in progress)

## 🔧 Troubleshooting

### CORS Error
If you see `CORS policy: No 'Access-Control-Allow-Origin' header`, ensure:
1. Backend is running on `http://localhost:8080`
2. Frontend is running on `http://localhost:4200`
3. Backend has the CORS configuration file in place

### Database Connection Error
If you get database connection errors:
1. Ensure MySQL is running
2. Check database credentials in `application.properties`
3. Verify the `product_manager` database exists

## 📦 Build for Production

### Backend
```bash
cd backend
./gradlew build
```
The JAR file will be created in `backend/build/libs/`

### Frontend
```bash
cd frontend
npm run build
```
The optimized build will be in `frontend/dist/`

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

**jmoreno-dev**

## 🤝 Contributing

Contributions are welcome! Feel free to submit issues and enhancement requests.

---

For more information about Angular, visit the [Official Angular Documentation](https://angular.io/docs).
