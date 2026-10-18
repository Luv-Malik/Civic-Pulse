# CivicPulse 🌍

CivicPulse is a civic-issue reporting and incident-triage MVP. Citizens can report potholes, water leaks, waste, broken streetlights, and other public issues. The backend groups likely duplicate reports using geographic distance and text similarity, calculates an explainable priority score, and provides a dashboard and map for triage.

> **Important:** This is a hackathon/learning MVP, not an official municipal service. Sample records are synthetic. Priority scores are heuristic estimates, not verified danger assessments. Do not upload sensitive personal information.

## Features

- Responsive React dashboard and report form
- Interactive OpenStreetMap using Leaflet
- Java 21 and Spring Boot REST API
- PostgreSQL database
- Duplicate detection using geographic distance and text similarity
- Explainable priority scoring based on severity, report count, and age
- Incident status and assignment updates
- Demo seed data and backend tests
- Docker Compose configuration for PostgreSQL

## Architecture

React/Vite → Spring Boot REST API → PostgreSQL

The MVP stores latitude and longitude as numeric fields and uses the Haversine formula to calculate distance. PostGIS can be added later for larger datasets and spatial indexing.

## Requirements

- Java 21
- Maven 3.9+
- Node.js 20+ and npm
- Docker Desktop

## Run Locally

### 1. Start PostgreSQL

From the project root directory:

```bash
docker compose up -d db
```

The default local development credentials are defined in `compose.yaml`. Do not use them in production.

### 2. Start the backend

Open a terminal:

```bash
cd backend
mvn spring-boot:run
```

Backend URL: http://localhost:8080

Health check: http://localhost:8080/api/health

The application creates database tables and seeds demo records automatically when the database is empty.

To disable demo seed data, set `CIVICPULSE_SEED=false`.

### 3. Start the frontend

Open a second terminal from the project root:

```bash
cd frontend
npm install
npm run dev
```

Open the URL printed by Vite, normally http://localhost:5173.

## Environment Variables

Backend variables:

| Variable | Default |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/civicpulse` |
| `DB_USERNAME` | `civicpulse` |
| `DB_PASSWORD` | `civicpulse_dev_password` |
| `CIVICPULSE_SEED` | `true` |
| `CORS_ORIGINS` | `http://localhost:5173` |

The frontend API URL defaults to `http://localhost:8080/api`. Set `VITE_API_URL` to override it.

## API Overview

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/health` | Health check |
| GET | `/api/reports` | List reports |
| POST | `/api/reports` | Submit a report |
| GET | `/api/reports/{id}` | Retrieve report details |
| PATCH | `/api/reports/{id}` | Update status or assignment |
| GET | `/api/incidents` | Group reports into incidents |
| GET | `/api/stats` | Dashboard metrics |

### Example Report

```json
{
  "title": "Large pothole near bus stop",
  "description": "Pothole on the left side of the road",
  "category": "POTHOLE",
  "severity": 4,
  "latitude": 28.9931,
  "longitude": 77.0151,
  "reporterName": "Demo Citizen"
}
```

Allowed categories: `POTHOLE`, `WATER_LEAK`, `GARBAGE`, `STREETLIGHT`, `DRAINAGE`, `OTHER`.

Statuses: `REPORTED`, `VERIFIED`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED`, `REJECTED`.

## How Duplicate Detection Works

1. Reports are considered candidates when they are within 250 metres.
2. Text similarity is calculated using token overlap (Jaccard similarity) over the title and description.
3. Reports are grouped when they are geographically close and meet the category and text-similarity rules.
4. Original citizen reports are preserved; incident groups are generated for display.

This is a transparent baseline approach and can produce false matches. Administrators should verify incident groups before taking action.

## Priority Score

The backend calculates a heuristic score from 0 to 100 using severity (up to 50 points), report count (up to 30 points), and age (up to 20 points). The API also provides an explanation.

This score is an estimate, not a safety-critical prediction.

## Run Tests

From the project root:

```bash
cd backend
mvn test
```

## Future Improvements

- Add authentication and role-based access control.
- Add secure image uploads and optional image classification with consent.
- Use PostGIS and spatial indexes for larger datasets.
- Add moderation, rate limiting, audit logs, and notifications.
- Evaluate duplicate detection using a labelled dataset.
- Integrate official municipal workflows only with permission.

## Limitations

CivicPulse is an educational MVP. Authentication, production-grade security, and operational municipal integrations must be implemented and reviewed before public deployment.
