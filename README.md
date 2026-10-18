<<<<<<< HEAD
# CivicPulse 🌍

CivicPulse is a civic-issue reporting and incident-triage MVP. Citizens can report potholes, water leaks, waste, broken streetlights and other public issues. The backend groups likely duplicate reports using geographic distance and text similarity, calculates an explainable priority score, and exposes a dashboard/map for triage.

> **Important:** This is a hackathon/learning MVP, not an official municipal service. The sample records are synthetic. Priority scores are heuristics, not verified danger assessments. Do not upload sensitive personal information.

## Features
- Responsive React dashboard and report form
- OpenStreetMap-powered interactive map (Leaflet)
- Java 21 + Spring Boot REST API
- PostgreSQL persistence
- Duplicate detection using distance + token similarity
- Explainable priority scoring (severity, affected reports, age)
- Admin-style status and assignment updates
- Demo seed data and backend tests
- Docker Compose for PostgreSQL

## Architecture

`React/Vite` → `Spring Boot REST API` → `PostgreSQL`

The MVP stores latitude/longitude as numeric fields and uses the Haversine formula for distance. This keeps local setup simple. PostGIS can be added later for larger datasets and spatial indexing.

## Requirements
- Java 21+
- Maven 3.9+ (or use your IDE's Maven support)
- Node.js 20+ and npm
- Docker Desktop (recommended) or local PostgreSQL 16

## Run locally

### 1. Start PostgreSQL
```bash
docker compose up -d db
```

Default local development credentials are in `compose.yaml`. Do not use them in production.

### 2. Start the backend
```bash
cd backend
mvn spring-boot:run
```
Backend: `http://localhost:8080`
API health: `http://localhost:8080/api/health`

The application creates tables and seeds demo records automatically when the database is empty. To disable demo seed data, set `CIVICPULSE_SEED=false`.

### 3. Start the frontend
Open a second terminal:
```bash
cd frontend
npm install
npm run dev
```
Open the URL printed by Vite, normally `http://localhost:5173`.

### Optional environment variables
Backend environment variables:
- `DB_URL` (default `jdbc:postgresql://localhost:5432/civicpulse`)
- `DB_USERNAME` (default `civicpulse`)
- `DB_PASSWORD` (default `civicpulse_dev_password`)
- `CIVICPULSE_SEED` (default `true`)
- `CORS_ORIGINS` (default `http://localhost:5173`)

Frontend API URL defaults to `http://localhost:8080/api`. Set `VITE_API_URL` before running Vite to override it.

## API overview

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/health` | Health check |
| GET | `/api/reports` | List reports (optional `?status=REPORTED`) |
| POST | `/api/reports` | Submit a report |
| GET | `/api/reports/{id}` | Report details |
| PATCH | `/api/reports/{id}` | Update status/assignment |
| GET | `/api/incidents` | Group reports into incidents |
| GET | `/api/stats` | Dashboard metrics |

Example report body:
```json
{
  "title": "Large pothole near bus stop",
  "description": "Deep pothole on the left side of the road",
  "category": "POTHOLE",
  "severity": 4,
  "latitude": 28.9931,
  "longitude": 77.0151,
  "reporterName": "Demo Citizen"
}
```

Allowed categories: `POTHOLE`, `WATER_LEAK`, `GARBAGE`, `STREETLIGHT`, `DRAINAGE`, `OTHER`.
Statuses: `REPORTED`, `VERIFIED`, `ASSIGNED`, `IN_PROGRESS`, `RESOLVED`, `REJECTED`.

## How duplicate detection works
1. Reports are candidates when they are within 250 metres.
2. Text similarity uses token overlap (Jaccard similarity) over title + description.
3. Reports are grouped when they are geographically close and either text similarity is high or their categories match and text similarity is moderate.
4. The dashboard groups reports at read time; original citizen reports are never deleted.

This is deliberately transparent baseline logic. It can make false matches, so admins should verify incident groups before taking action. A production version should use robust geospatial indexing, calibrated thresholds, abuse prevention, image verification and human review.

## Priority score
The backend produces a 0–100 heuristic score from severity (0–50), number of reports (0–30), and age (0–20). The API returns a plain-language explanation. It is not a safety-critical prediction.

## Tests
```bash
cd backend
mvn test
```

## GitHub setup
```bash
git init
git add .
git commit -m "Build CivicPulse civic issue triage MVP"
git branch -M main
# Create an empty repository on GitHub, then use its URL below:
git remote add origin https://github.com/YOUR-USERNAME/civicpulse.git
git push -u origin main
```

## Next improvements
- Add authentication and role-based access control before public deployment.
- Add image upload storage and image classification with user consent.
- Add PostGIS geography columns and spatial indexes for scale.
- Add moderation/rate limiting, audit logs and notification integrations.
- Add a proper test dataset and measure duplicate-detection precision/recall.
- Integrate official municipal workflows only with permission.
=======
# Civic-Pulse
Combines similar reports about water leakage, blocked drains, streetlights or damaged roads into a shared incident map. Detect clusters and estimate which issues deserve attention first.
>>>>>>> 86f8031752e8bc1a521c05c28db2859a7489e96c
