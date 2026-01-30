-- Script de inicialización de base de datos para DevSu
-- Este script crea las tablas necesarias para ambos microservicios

USE devsu_db;

-- Tabla: personas (base para herencia)
CREATE TABLE IF NOT EXISTS personas (
    persona_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20) NOT NULL,
    edad INT NOT NULL,
    identificacion VARCHAR(50) NOT NULL UNIQUE,
    direccion VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    tipo_persona VARCHAR(50) NOT NULL,
    cliente_id VARCHAR(50) UNIQUE,
    contrasena VARCHAR(255),
    estado BOOLEAN DEFAULT TRUE,
    INDEX idx_identificacion (identificacion),
    INDEX idx_cliente_id (cliente_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: cuentas
CREATE TABLE IF NOT EXISTS cuentas (
    cuenta_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    numero_cuenta VARCHAR(50) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(19, 2) NOT NULL,
    saldo_disponible DECIMAL(19, 2) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    cliente_id VARCHAR(50) NOT NULL,
    INDEX idx_numero_cuenta (numero_cuenta),
    INDEX idx_cliente_id (cliente_id),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: movimientos
CREATE TABLE IF NOT EXISTS movimientos (
    movimiento_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fecha DATETIME NOT NULL,
    tipo_movimiento VARCHAR(50) NOT NULL,
    valor DECIMAL(19, 2) NOT NULL,
    saldo_disponible DECIMAL(19, 2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    FOREIGN KEY (cuenta_id) REFERENCES cuentas(cuenta_id) ON DELETE CASCADE,
    INDEX idx_cuenta_id (cuenta_id),
    INDEX idx_fecha (fecha),
    INDEX idx_tipo_movimiento (tipo_movimiento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar datos de prueba
-- Cliente de prueba
INSERT INTO personas (nombre, genero, edad, identificacion, direccion, telefono, tipo_persona, cliente_id, contrasena, estado)
VALUES (
    'Juan Pérez',
    'Masculino',
    35,
    '1234567890',
    'Calle Principal 123',
    '5551234567',
    'CLIENTE',
    'CLIENTE001',
    'password123',
    TRUE
);

-- Cuenta de prueba
INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id)
VALUES (
    '1001-001-000001',
    'Ahorros',
    1000.00,
    1000.00,
    TRUE,
    'CLIENTE001'
);

-- Movimiento de prueba
INSERT INTO movimientos (fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id)
VALUES (
    NOW(),
    'Credito',
    1000.00,
    1000.00,
    1
);
