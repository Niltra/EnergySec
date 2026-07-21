<div align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot" alt="Spring Boot 3" />
  <img src="https://img.shields.io/badge/Python-3.11-3776AB?style=for-the-badge&logo=python" alt="Python 3.11" />
  <img src="https://img.shields.io/badge/Docker-Enterprise-2496ED?style=for-the-badge&logo=docker" alt="Docker" />
  <img src="https://img.shields.io/badge/QA_Tests-100%25_Passed-success?style=for-the-badge&logo=githubactions" alt="QA Passed" />
</div>

<h1 align="center">⚡ EnergySec Platform</h1>

<p align="center">
  <em>Enterprise-grade Geo-Risk & Cyber-Intelligence System for Critical Infrastructures.</em><br>
  <em>Plataforma empresarial de Inteligencia Geoespacial y Ciberseguridad para Infraestructuras Críticas.</em>
</p>

---

## 🌍 The Pitch / El Problema a Resolver

**[EN]** In an era where energy infrastructure is critical, managing massive real-time data while ensuring national security is paramount. **EnergySec** ingests thousands of metrics per minute from public grids (like ESIOS), processes them through a Scikit-Learn **AI Engine** (Isolation Forest) to detect anomalies, and uses a mathematical **Geo-Risk Engine** to cross-reference location data with external events (e.g. Traffic Accidents). But most importantly: it uses advanced **Aspect-Oriented Programming (AOP)** to dynamically obfuscate confidential financial and location data based on the user's clearance level.

**[ES]** En la era de la ciberseguridad industrial, gestionar datos masivos en tiempo real es vital. **EnergySec** ingesta miles de métricas energéticas, las procesa mediante un motor de **Inteligencia Artificial** para detectar anomalías, y cruza las coordenadas exactas de las infraestructuras con alertas públicas (ej. Accidentes DGT) mediante su **Motor Geo-Riesgo**. ¿El verdadero valor comercial? Un sistema de **Ciberseguridad en Tiempo Real (AOP)** que ofusca (censura) automáticamente los datos financieros y catastrales confidenciales dependiendo del nivel de autorización del empleado, previniendo fugas de información.

---

## ⚙️ The Engine Room / La Sala de Máquinas (Tech Stack)

### 1. Hexagonal Architecture (Ports & Adapters)
We protect the domain logic at all costs. The core is completely framework-agnostic, interacting with the outside world (PostgreSQL, Python ML, REST APIs) exclusively through strictly defined Ports and Adapters.

### 2. High Availability with Virtual Threads (Project Loom)
Designed for massive scale. By leveraging **Java 21 Virtual Threads**, the Spring Boot backend can spawn tens of thousands of concurrent non-blocking threads with negligible memory footprint. Combined with **Hibernate Batching** and **Bucket4j Rate Limiting**, the API is fully shielded against DDoS attacks and database bottlenecks.

### 3. DevSecOps & Dynamic Data Masking
Our crown jewel. Instead of scattering `if(user.isAdmin())` checks throughout the codebase, we developed a dynamic **Security Aspect (AOP)**. It intercepts JSON responses right before they leave the backend. If the virtual thread determines the user lacks clearance, critical data (CIF/NIF, monetary values, exact coordinates) is instantly obfuscated (e.g. `1.2M €` becomes `[CONFIDENTIAL]`).

### 4. AI Engine (Isolation Forest)
An isolated Python 3.11 microservice that consumes the electrical grid readings and applies Scikit-Learn's *Isolation Forest* algorithm to flag sudden drops or unnatural peaks in energy consumption, communicating back to Java via a resilient `Resilience4j` Circuit Breaker.

---

## 🚀 Quick Start (Plug & Play)

The entire ecosystem is containerized and orchestrated. No complex setups needed.
*Todo el ecosistema está orquestado. No necesitas instalaciones complejas.*

```bash
# 1. Spin up the entire infrastructure (Java, Python, DB, Prometheus, Grafana)
docker-compose up -d --build

# 2. Check the logs and verify the containers are healthy
docker-compose ps
```

### 📍 Access Points / Puntos de Acceso

- **💻 Interactive Frontend (Map UI):** Open `frontend/index.html` in any browser to test the Data Masking in real-time.
- **📖 Swagger UI (API Docs):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **📊 Grafana Dashboards:** [http://localhost:3000](http://localhost:3000) (User: `admin` / Password: `admin`)
- **🔎 Prometheus Metrics:** [http://localhost:9090](http://localhost:9090)

---
<div align="center">
  <i>Designed and Built by an ambitious Software Engineer focusing on Enterprise Architecture & Cloud Security.</i>
</div>
