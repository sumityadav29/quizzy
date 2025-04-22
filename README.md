# Quizzy

A multiplayer quiz application built with React and Spring Boot.

## Overview

Quizzy is a web application that allows users to participate in multiplayer quizzes in real-time. The application features a React-based frontend and a Spring Boot backend, with SSE support for real-time communication.

## Tech Stack

### Frontend
- React 19
- TypeScript
- React Router
- Axios

### Backend
- Spring Boot 3.4.4
- Spring Web
- Spring Data JPA
- Spring WebSocket
- PostgreSQL

## Prerequisites

Runnning the application locally needs nothing bit Docker installed

## Getting Started

### Using Docker (Recommended)

```bash
docker-compose up --build
```

The application will be available at:
- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- API Documentation: http://localhost:8080/swagger-ui.html

### Manual Setup

#### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build and run the Spring Boot application:
```bash
./mvnw spring-boot:run
```

#### Frontend Setup

1. Navigate to the UI directory:
```bash
cd ui
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

## Project Structure

```
quizzy/
├── backend/           # Spring Boot backend
│   ├── src/          # Source code
│   ├── Dockerfile    # Backend container configuration
│   └── pom.xml       # Maven dependencies
├── ui/               # React frontend
│   ├── src/          # Source code
│   ├── public/       # Static assets
│   └── package.json  # Node.js dependencies
└── docker-compose.yml # Container orchestration
```

## Features

- Real-time multiplayer quiz functionality
- WebSocket-based communication
- RESTful API endpoints
- Modern React-based user interface
- PostgreSQL database integration
- Swagger API documentation

## Development

### Building for Production

#### Frontend
```bash
cd ui
npm run build
```

#### Backend
```bash
cd backend
./mvnw clean package -DskipTests
```
