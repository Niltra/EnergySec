<div align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=spring-boot" alt="Spring Boot 3" />
  <img src="https://img.shields.io/badge/Python-3.11-3776AB?style=for-the-badge&logo=python" alt="Python 3.11" />
  <img src="https://img.shields.io/badge/Docker-Enterprise-2496ED?style=for-the-badge&logo=docker" alt="Docker" />
  <img src="https://img.shields.io/badge/QA_Tests-100%25_Passed-success?style=for-the-badge&logo=githubactions" alt="QA Passed" />
</div>

<h1 align="center">⚡ EnergySec Platform</h1>

<p align="center">
  <em>Plataforma empresarial de Inteligencia Geoespacial y Ciberseguridad para Infraestructuras Críticas.</em><br>
  <em>Enterprise-grade Geo-Risk & Cyber-Intelligence System for Critical Infrastructures.</em>
</p>

---

# 🇪🇸 VERSIÓN EN ESPAÑOL

## 🌍 El Problema a Resolver (Pitch Comercial)

En la era de la ciberseguridad industrial y la transición energética, gestionar datos masivos en tiempo real es vital. **EnergySec** ingesta miles de métricas energéticas, las procesa mediante un motor de **Inteligencia Artificial** para detectar anomalías de consumo o voltaje, y cruza las coordenadas exactas de las infraestructuras con alertas públicas (ej. Accidentes de Tráfico de la DGT) mediante su **Motor Geo-Riesgo**. 

¿El verdadero valor comercial y de seguridad? Un sistema de **Ciberseguridad en Tiempo Real basado en Aspectos (AOP)** que ofusca y censura automáticamente los datos financieros y catastrales confidenciales dependiendo del nivel de autorización del empleado, previniendo fugas de información interna (Data Leaks) sin penalizar el rendimiento del código.

## ⚙️ La Sala de Máquinas (Arquitectura y Tecnologías)

### 1. Arquitectura Hexagonal (Ports & Adapters)
Hemos protegido la lógica de negocio a toda costa. El núcleo (Core Domain) de Java es completamente agnóstico al framework. Interactúa con el mundo exterior (Bases de Datos PostgreSQL, Microservicio Python ML, y APIs REST) de forma exclusiva a través de Puertos (Interfaces) y Adaptadores estrictamente definidos.

### 2. Alta Disponibilidad con Virtual Threads (Project Loom)
El sistema está diseñado para escalar de forma masiva. Al utilizar **Virtual Threads de Java 21**, el backend Spring Boot puede levantar decenas de miles de hilos asíncronos concurrentes con un impacto en memoria insignificante. Combinado con la agrupación masiva de persistencia (**Hibernate Batching** optimizado en bloques de 100) y un filtro defensivo de peticiones (**Bucket4j Rate Limiting** por Tokens), la API está completamente blindada contra cuellos de botella y ataques de Denegación de Servicio (DDoS).

### 3. DevSecOps y Dynamic Data Masking (Enmascaramiento de Datos)
Nuestra joya de la corona. En lugar de dispersar sentencias condicionales `if (user.isAdmin())` por todo el código, desarrollamos un **Security Aspect (AOP)** dinámico. Este aspecto intercepta los objetos JSON de respuesta justo antes de que abandonen el servidor backend. Si el hilo virtual determina que el JWT del usuario carece de los privilegios adecuados, los datos críticos (CIF/NIF de empresas, valores monetarios en euros, y coordenadas geográficas exactas) son ofuscados instantáneamente (ej. `1.250.000 €` se transforma en `[CONFIDENCIAL]`).

### 4. Motor de Inteligencia Artificial (Isolation Forest)
Un microservicio aislado escrito en Python 3.11 (FastAPI) que consume las lecturas de la red eléctrica y aplica el algoritmo de Machine Learning *Isolation Forest* (Scikit-Learn). Identifica picos de tensión antinaturales o caídas de demanda en microsegundos, y se comunica con el ecosistema Java de vuelta mediante un cortacircuitos resiliente (**Resilience4j Circuit Breaker**), lo que previene que la caída de la IA afecte a la infraestructura global (prevención de *Alert Fatigue*).

### 5. Observabilidad Total
Monitorizamos el corazón del sistema en vivo usando **Spring Boot Actuator** en conjunto con un recolector de métricas que alimenta un contenedor **Prometheus**. Estos datos (uso de hilos, pools de conexiones HikariCP, latencias HTTP) se visualizan en un dashboard empresarial mediante **Grafana**.

## 🚀 Guía Rápida de Inicio (Plug & Play)

Para que cualquier persona pueda clonar y arrancar el ecosistema en 1 minuto sin configurar variables de entorno, hemos preparado unos scripts de auto-arranque que generan secretos locales de un solo uso y levantan los contenedores. Solo necesitas tener Docker instalado.

### En Windows 🪟
Simplemente haz **doble clic** en el archivo `start.bat` o ejecútalo en tu terminal:
```cmd
start.bat
```

### En Linux / Mac 🍎🐧
Ejecuta el script de bash:
```bash
chmod +x start.sh
./start.sh
```

El script se encargará de crear la carpeta `secrets/` localmente y levantar la base de datos, el backend Java, el motor Python y el panel Grafana.

### 📍 Puntos de Acceso Directo

- **💻 Frontend Interactivo (Mapa UI):** Abre el archivo `frontend/index.html` en tu navegador con doble clic para probar el AOP de enmascaramiento de datos en tiempo real sobre el mapa de España.
- **📖 Swagger UI (Documentación de API):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **📊 Grafana Dashboards:** [http://localhost:3000](http://localhost:3000) *(Usuario: `admin` / Contraseña: `admin`)*
- **🔎 Métricas Prometheus:** [http://localhost:9090](http://localhost:9090)

---
<br>

# 🇬🇧 ENGLISH VERSION

## 🌍 The Pitch (Problem to Solve)

In an era where energy infrastructure and cyber warfare are critical, managing massive real-time data while ensuring national security is paramount. **EnergySec** ingests thousands of metrics per minute from public grids, processes them through an **AI Engine** to detect voltage/consumption anomalies, and uses a mathematical **Geo-Risk Engine** to cross-reference precise infrastructural coordinates with external public alerts (e.g. Traffic Accidents). 

But most importantly, its true business value lies in a real-time **Aspect-Oriented Programming (AOP) Cybersecurity System** that dynamically obfuscates confidential financial and cadastre data based on the employee's authorization level, preventing internal data leaks without any performance penalties.

## ⚙️ The Engine Room (Architecture and Technologies)

### 1. Hexagonal Architecture (Ports & Adapters)
We protect the domain logic at all costs. The Java Core Domain is completely framework-agnostic, interacting with the outside world (PostgreSQL Databases, Python ML Microservices, REST APIs) exclusively through strictly defined Ports and Adapters.

### 2. High Availability with Virtual Threads (Project Loom)
Designed for massive scale. By leveraging **Java 21 Virtual Threads**, the Spring Boot backend can spawn tens of thousands of concurrent non-blocking threads with a negligible memory footprint. Combined with bulk persistence optimization (**Hibernate Batching**) and a defensive IP request filter (**Bucket4j Token-based Rate Limiting**), the API is fully shielded against DDoS attacks and database bottlenecks.

### 3. DevSecOps & Dynamic Data Masking
Our crown jewel. Instead of scattering `if(user.isAdmin())` checks throughout the codebase, we developed a dynamic **Security Aspect (AOP)**. It intercepts JSON responses right before they leave the backend server. If the virtual thread determines the user's JWT lacks sufficient clearance, critical data (Company CIFs, monetary values, exact GPS coordinates) is instantly obfuscated (e.g. `1.2M €` becomes `[CONFIDENTIAL]`).

### 4. AI Engine (Isolation Forest)
An isolated Python 3.11 microservice that consumes the electrical grid readings and applies Scikit-Learn's *Isolation Forest* algorithm to flag sudden drops or unnatural peaks in energy consumption in microseconds. It communicates back to the Java ecosystem via a resilient **Resilience4j Circuit Breaker**, preventing an AI outage from cascading into a global infrastructure failure (preventing *Alert Fatigue*).

### 5. Full Observability
We monitor the system's heartbeat live using **Spring Boot Actuator** combined with a metrics scraper that feeds a **Prometheus** container. This data (thread usage, HikariCP connection pools, HTTP latencies) is visualized on an enterprise dashboard via **Grafana**.

## 🚀 Quick Start (Plug & Play)

To make it incredibly easy for anyone to clone and spin up the ecosystem in 1 minute without setting up complex environment variables, we've provided auto-start scripts that generate local dummy secrets and orchestrate the containers. You only need Docker installed.

### On Windows 🪟
Simply **double-click** the `start.bat` file or run it in your terminal:
```cmd
start.bat
```

### On Linux / Mac 🍎🐧
Run the bash script:
```bash
chmod +x start.sh
./start.sh
```

The script will automatically handle the local `secrets/` creation and spin up the Database, Java Backend, Python Engine, and Grafana dashboard.

### 📍 Access Points

- **💻 Interactive Frontend (Map UI):** Open the `frontend/index.html` file in your browser to test the Data Masking AOP in real-time over the map of Spain.
- **📖 Swagger UI (API Docs):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **📊 Grafana Dashboards:** [http://localhost:3000](http://localhost:3000) *(User: `admin` / Password: `admin`)*
- **🔎 Prometheus Metrics:** [http://localhost:9090](http://localhost:9090)

---
<div align="center">
  <i>Designed and Built by an ambitious Software Engineer focusing on Enterprise Architecture & Cloud Security.</i>
</div>
