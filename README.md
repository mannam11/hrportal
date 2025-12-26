# HR Portal – Recruitment Management Backend

## Overview
The **HR Portal** is a backend service designed to support end-to-end recruitment workflows for organizations. It provides secure, scalable APIs for recruiters to create job postings, manage candidate applications, track hiring activity through dashboards, and export application data.

This repository focuses purely on **backend responsibilities**, including authentication, authorization, data modeling, business logic, and performance-conscious API design.

---

## Problem Statement
Recruitment teams often rely on fragmented tools to manage job postings, candidate applications, and hiring metrics. These systems frequently lack:
- Clear ownership boundaries between recruiters
- Secure and scalable authentication mechanisms
- Efficient querying for dashboards and analytics
- Simple, reliable data export capabilities

This backend service addresses those gaps by providing a **centralized, secure, recruiter-owned API layer** that can be consumed by any frontend or external system.

---

## Core Features

### Authentication & Authorization
- JWT-based access token authentication
- Refresh token mechanism using HTTP-only cookies
- Token rotation and revocation support
- Secure logout flow
- Recruiter-scoped access to all protected resources

### Job Post Management
- Create and update job postings
- Open / close job lifecycle management
- Recruiter ownership enforced via `createdBy`
- Fetch all job posts created by a recruiter

### Application Management
- Candidate application submission (resume upload supported)
- Duplicate application prevention per job post
- Application status tracking
- Recruiter-scoped access to applications

### Dashboard & Analytics
- Total and active job post counts
- Application counts per job post
- Application source distribution
- Recent applications support via time-based queries

### Data Export
- Secure CSV export of applications by job post
- Status-based filtering during export
- Server-side CSV generation

---

## Architecture Overview
The application follows a **layered, stateless backend architecture**:

- **Controller Layer** – Exposes RESTful APIs and handles request validation
- **Service Layer** – Encapsulates business logic and authorization rules
- **Repository Layer** – Handles persistence using MongoDB
- **Security Layer** – JWT validation, refresh handling, and request filtering

Asynchronous tasks (such as application analysis) are delegated to a queue to keep API responses fast and reliable.

---

## Authentication & Security Model

### Access Tokens
- Short-lived JWT access tokens
- Contain user identity and role claims
- Sent via `Authorization: Bearer <token>` header

### Refresh Tokens
- Long-lived refresh tokens
- Stored securely as **hashed values** in the database
- Delivered to clients via **HTTP-only cookies**
- Used to issue new access tokens without re-authentication

### Logout & Revocation
- Refresh tokens are invalidated on logout
- Expired or invalid refresh tokens force re-login

This model ensures strong security while maintaining a stateless API design.

---

## API Design Principles

- RESTful resource-oriented endpoints
- Clear separation of public and protected APIs
- Ownership-based access control (via `createdBy`)
- No `findAll` usage for sensitive or large datasets
- Pagination and filtering used wherever applicable

Primary API domains:
- `/auth` – Authentication and token lifecycle
- `/job-posts` – Job post management
- `/applications` – Application management
- `/dashboard` – Aggregated recruiter insights

---

## Data Model Overview

### User
Represents a recruiter using the system.
- Authenticated via email and password
- Assigned a role (e.g., `RECRUITER`)

### JobPost
Represents an open or closed hiring position.
- Owned by a recruiter (`createdBy`)
- Stores role details, skills, experience range, and status

### Application
Represents a candidate application to a job post.
- Linked to a job via `jobId`
- Inherits recruiter ownership from the job post
- Tracks candidate details, status, and source

### RefreshToken
- Stores hashed refresh tokens
- Linked to a user
- Supports expiration and revocation

This structure ensures **multi-tenant safety** and prevents cross-recruiter data access.

---

## Dashboard Aggregations (Backend)

The dashboard APIs provide recruiter-specific insights such as:
- Total job posts
- Active job posts
- Total applications received
- Application source distribution
- Recent applications using time-based queries

To support near–real-time updates, the backend exposes incremental endpoints (e.g., applications created after a timestamp), enabling efficient polling without heavy database scans.

---

## Export Functionality

The backend supports exporting application data as CSV files:
- Filtered by job post and application status
- Generated server-side for consistency and security
- Protected by recruiter ownership checks

This allows seamless integration with external reporting or HR tools.

---
## 🔌 External Integrations

This project integrates with several external services to support scalable, production-grade backend workflows. These integrations are intentionally designed to be asynchronous, secure, and loosely coupled.

### AWS S3 – Resume Storage
- Candidate resumes are uploaded and stored in **AWS S3**.
- Only the generated S3 object key is persisted in the database.
- This keeps the database lightweight and allows the system to scale independently for file storage.
- Access to resumes is fully controlled by the backend.

### AWS SQS – Asynchronous Processing
- After a candidate applies for a job, an event is published to **AWS SQS**.
- This enables non-blocking, asynchronous processing for downstream tasks.
- The main application flow remains fast and resilient, even under high load.

### OpenAI – Resume Analysis
- Resume analysis is performed asynchronously using **OpenAI APIs**.
- The analysis extracts structured insights from resumes, which are stored and used for:
    - Automated shortlisting
    - Application insights
    - Dashboard metrics
- AI processing is fully decoupled from the core request lifecycle.