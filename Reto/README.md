# Proyecto Reto DevSu - Microservicios con Arquitectura Hexagonal

## Descripción

Implementación de dos microservicios independientes que funcionan con arquitectura hexagonal, MySQL compartida y comunicación asincrónica mediante RabbitMQ.

### Servicios

1. **Cliente-Persona Service** (Puerto 8081)
   - CRUD de Clientes y Personas
   - Herencia JPA con tabla única (SINGLE_TABLE)
   - Publicación de eventos de cliente

2. **Cuenta-Movimientos Service** (Puerto 8082)
   - CRUD de Cuentas y Movimientos
   - Débitos y créditos con validación de saldo
   - Escucha eventos de clientes

## Arquitectura

```
Domain Layer (Dominio)
├── Entidades (JPA)
└── Puertos (Interfaces)

Application Layer (Aplicación)
└── Casos de Uso (Servicios)

Infrastructure Layer (Infraestructura)
├── Adaptadores (JPA, RabbitMQ)
├── Controladores (REST)
└── Configuración
```

## Requisitos

- Docker y Docker Compose
- Java 11+ (opcional, para desarrollo local)
- Maven 3.6+ (opcional, para desarrollo local)

## Instalación y Ejecución

### 1. Compilar Proyectos (Opcional - Docker lo hace automáticamente)

```bash
cd /Users/angelduran/Documents/Reto\ DevSu/Reto
mvn clean package -DskipTests
```

### 2. Ejecutar con Docker Compose

```bash
cd /Users/angelduran/Documents/Reto\ DevSu/Reto
docker-compose up -d
```

Este comando iniciará:
- MySQL 8.0 en puerto 3306
- RabbitMQ en puertos 5672 (AMQP) y 15672 (Management UI)
- Cliente-Persona Service en puerto 8081
- Cuenta-Movimientos Service en puerto 8082

### 3. Verificar Servicios

- RabbitMQ Management: http://localhost:15672 (guest/guest)
- MySQL: localhost:3306 (devsu_user/devsu_password)

## Endpoints API

### Cliente-Persona Service (Puerto 8081)

#### Clientes

- **GET** `/api/clientes` - Obtener todos los clientes
- **GET** `/api/clientes/{id}` - Obtener cliente por ID
- **GET** `/api/clientes/identificacion/{clienteId}` - Obtener por clienteId
- **POST** `/api/clientes` - Crear cliente
- **PUT** `/api/clientes/{id}` - Actualizar cliente
- **PATCH** `/api/clientes/{id}` - Actualizar estado/contraseña
- **DELETE** `/api/clientes/{id}` - Eliminar cliente

#### Ejemplo Request (POST)

```json
{
  "nombre": "Maria González",
  "genero": "Femenino",
  "edad": 28,
  "identificacion": "9876543210",
  "direccion": "Calle Secundaria 456",
  "telefono": "5559876543",
  "clienteId": "CLIENTE002",
  "contrasena": "password123",
  "estado": true
}
```

### Cuenta-Movimientos Service (Puerto 8082)

#### Cuentas

- **GET** `/api/cuentas` - Obtener todas las cuentas
- **GET** `/api/cuentas/{id}` - Obtener cuenta por ID
- **GET** `/api/cuentas/numero/{numeroCuenta}` - Obtener por número
- **GET** `/api/cuentas/cliente/{clienteId}` - Obtener cuentas del cliente
- **POST** `/api/cuentas` - Crear cuenta
- **PUT** `/api/cuentas/{id}` - Actualizar cuenta
- **DELETE** `/api/cuentas/{id}` - Eliminar cuenta

#### Movimientos

- **GET** `/api/movimientos` - Obtener todos los movimientos
- **GET** `/api/movimientos/{id}` - Obtener movimiento por ID
- **GET** `/api/movimientos/cuenta/{cuentaId}` - Movimientos de una cuenta
- **POST** `/api/movimientos/cuenta/{cuentaId}` - Registrar débito/crédito

#### Ejemplo Request (POST Cuenta)

```json
{
  "numeroCuenta": "1001-001-000002",
  "tipoCuenta": "Corriente",
  "saldoInicial": 5000.00,
  "clienteId": "CLIENTE002",
  "estado": true
}
```

#### Ejemplo Request (POST Movimiento)

```json
{
  "tipoMovimiento": "Debito",
  "valor": 100.00
}
```

## Estructura de Directorios

```
Reto/
├── pom.xml (Parent POM)
├── docker-compose.yml
├── init.sql
├── README.md
├── cliente-persona-service/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/
│       ├── main/
│       │   ├── java/org/devsu/com/clientepersona/
│       │   │   ├── ClientePersonaApplication.java
│       │   │   ├── domain/
│       │   │   │   ├── entity/ (Persona, Cliente)
│       │   │   │   └── port/ (ClienteRepository, ClienteEventPublisher)
│       │   │   ├── application/
│       │   │   │   └── usecase/ (ClienteService)
│       │   │   └── infrastructure/
│       │   │       ├── adapter/ (Repositories, RabbitMQ Adapter)
│       │   │       ├── config/ (RabbitMQConfig)
│       │   │       ├── controller/ (ClienteController)
│       │   │       └── dto/ (Request/Response)
│       │   └── resources/
│       │       └── application.yml
│       └── test/
│
└── cuenta-movimientos-service/
    ├── pom.xml
    ├── Dockerfile
    └── src/
        ├── main/
        │   ├── java/org/devsu/com/cuentamovimientos/
        │   │   ├── CuentaMovimientosApplication.java
        │   │   ├── domain/
        │   │   │   ├── entity/ (Cuenta, Movimiento)
        │   │   │   └── port/ (Repositories)
        │   │   ├── application/
        │   │   │   └── usecase/ (CuentaService, MovimientoService)
        │   │   └── infrastructure/
        │   │       ├── adapter/ (Repositories, RabbitMQ Listener)
        │   │       ├── config/ (RabbitMQConfig)
        │   │       ├── controller/ (CuentaController, MovimientoController)
        │   │       └── dto/ (Request/Response)
        │   └── resources/
        │       └── application.yml
        └── test/
```

## Eventos RabbitMQ

### Exchange: `cliente.exchange`

#### Routing Keys

- `cliente.created` - Se publica cuando se crea un cliente
- `cliente.updated` - Se publica cuando se actualiza un cliente
- `cliente.deleted` - Se publica cuando se elimina un cliente

#### Queues en Cuenta-Movimientos Service

- `cuenta.cliente.created.queue` - Escucha creación de clientes
- `cuenta.cliente.updated.queue` - Escucha actualizaciones de clientes
- `cuenta.cliente.deleted.queue` - Escucha eliminación de clientes

## Validaciones de Negocio

### Cliente-Persona Service

- Identificación única
- ClienteId único
- Campos requeridos validados con JSR-303
- Contraseña obligatoria para clientes

### Cuenta-Movimientos Service

- Número de cuenta único
- Saldo inicial >= 0
- Débitos validados contra saldo disponible
- Movimientos registran saldo después de la transacción

## Detener Servicios

```bash
docker-compose down
```

Para limpiar volúmenes:

```bash
docker-compose down -v
```

## Logs

Ver logs de un servicio específico:

```bash
docker-compose logs -f cliente-persona-service
docker-compose logs -f cuenta-movimientos-service
docker-compose logs -f mysql
docker-compose logs -f rabbitmq
```

## Desarrollo Local (sin Docker)

1. Instalar MySQL 8.0 y RabbitMQ localmente
2. Crear base de datos `devsu_db` e importar `init.sql`
3. Actualizar `application.yml` con URLs locales
4. Compilar: `mvn clean package`
5. Ejecutar servicios en terminales separadas:
   ```bash
   java -jar cliente-persona-service/target/cliente-persona-service-1.0-SNAPSHOT.jar
   java -jar cuenta-movimientos-service/target/cuenta-movimientos-service-1.0-SNAPSHOT.jar
   ```

## Tecnologías Utilizadas

- **Spring Boot 2.7.14** - Framework base
- **Spring Data JPA** - ORM
- **Spring AMQP** - RabbitMQ
- **MySQL 8.0** - Base de datos
- **RabbitMQ 3.12** - Message broker
- **Lombok** - Reducción de boilerplate
- **Maven** - Gestor de dependencias
- **Docker & Docker Compose** - Containerización
