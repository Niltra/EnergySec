-- init.sql: Seed data inicial para la plataforma EnergySec

-- Creamos una tabla básica de usuarios (aunque la autenticación actual sea JWT stateless,
-- sirve para tener datos maestros para los dashboards de Grafana).
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, role) VALUES ('admin_user', 'ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO users (username, role) VALUES ('basic_user', 'ROLE_BASIC') ON CONFLICT DO NOTHING;
INSERT INTO users (username, role) VALUES ('finance_auditor', 'ROLE_FINANCE') ON CONFLICT DO NOTHING;

-- Creación de la tabla de métricas (Hibernate la actualizará si difiere)
CREATE TABLE IF NOT EXISTS energy_metrics (
    id VARCHAR(255) PRIMARY KEY,
    asset_id VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    is_anomaly BOOLEAN NOT NULL,
    readings JSONB
);

-- Inyección de Métricas Históricas para los Dashboards
INSERT INTO energy_metrics (id, asset_id, timestamp, is_anomaly, readings) 
VALUES 
('metric-001', 'SUBSTATION-MAD', NOW() - INTERVAL '1 hour', false, '[30000.5, 30100.2]'),
('metric-002', 'SUBSTATION-MAD', NOW() - INTERVAL '45 minutes', false, '[29500.0, 29800.0]'),
('metric-003', 'PLANT-BCN', NOW() - INTERVAL '30 minutes', false, '[31000.1, 31200.5]'),
('metric-004', 'PLANT-BCN', NOW() - INTERVAL '15 minutes', true, '[15000.0, 14500.0]'), -- Anomalía (Caída)
('metric-005', 'SUBSTATION-VAL', NOW() - INTERVAL '5 minutes', true, '[45000.0, 46000.0]') -- Anomalía (Pico)
ON CONFLICT DO NOTHING;
