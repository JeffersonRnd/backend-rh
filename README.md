# Hotel Reservas - Backend

API REST para la gestión de reservas hoteleras, desarrollada con Spring Boot.

## Tecnologías utilizadas
- Java 21 + Spring Boot 3.3.5
- Spring Data JPA + Hibernate
- Spring Security + JWT (autenticación y autorización por roles)
- Spring HATEOAS
- MySQL (base de datos: hotelreservas_bd, puerto 3306)
- Lombok
- Servidor en el puerto 9090

## Arquitectura
El proyecto implementa una arquitectura en capas con clases genéricas reutilizables:
- **Controller** — gestiona las peticiones HTTP, valida DTOs (`@Valid`) y delega al servicio correspondiente
- **Service / IGenericService + GenericServiceImpl** — interfaz y clase abstracta genérica con operaciones CRUD
  (`listarTodos`, `buscarPorId`, `guardar`, `actualizar`, `eliminar`) reutilizadas por todos los servicios concretos
- **Repository / IGenericRepository** — interfaz base genérica (`@NoRepositoryBean`) extendida por todos los repositorios JPA
- **DTOs** — Request/Response DTOs separan la capa de persistencia de la API pública, evitando exponer entidades JPA
- **Mapper** — clases utilitarias que convierten entidades <-> DTOs
- **GlobalExceptionHandler** — maneja `ResourceNotFoundException` (404), `DuplicateResourceException` (409),
  `BadRequestException` / errores de validación (400), `BadCredentialsException` (401), `AccessDeniedException` (403)
  y errores genéricos (500)

## Entidades del dominio
- `TipoHabitacion` — categoriza las habitaciones (Simple, Doble, Suite)
- `Habitacion` — registra número, precio por noche, capacidad, servicios y disponibilidad
- `Huesped` — almacena los datos del cliente con DNI y correo únicos
- `Reserva` — vincula al huésped con las habitaciones reservadas y calcula el total
- `DetalleReserva` — representa cada habitación dentro de una reserva
- `Usuario` — credenciales de acceso (username/password) con rol `ADMIN` o `HUESPED`, opcionalmente
  vinculado a un `Huesped`

## Nivel de madurez Richardson
- **Nivel 2**: recursos con URIs (`/api/...`), uso correcto de verbos HTTP (GET, POST, PUT, DELETE)
  y códigos de estado (200, 201, 204, 400, 404, 409, etc.)
- **Nivel 3 (HATEOAS)**: `ReservaController` devuelve `EntityModel`/`CollectionModel` con enlaces `self`,
  `reservas`, `detalles` y `huesped`, permitiendo navegar la API a partir de las respuestas.

## Autenticación y autorización (JWT)

### Endpoints públicos
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/register` | Registrar un usuario (`rol`: `ADMIN` o `HUESPED`) |
| POST | `/api/auth/login` | Iniciar sesión, devuelve un JWT |

Ejemplo de registro de huésped:
```json
POST /api/auth/register
{
  "username": "juanperez",
  "password": "123456",
  "rol": "HUESPED",
  "nombre": "Juan",
  "apellido": "Pérez",
  "dni": "12345678",
  "correo": "juan@example.com",
  "telefono": "999999999",
  "direccion": "Av. Siempre Viva 123"
}
```

Ejemplo de login:
```json
POST /api/auth/login
{
  "username": "juanperez",
  "password": "123456"
}
```
Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "juanperez",
  "rol": "HUESPED"
}
```

Para consumir el resto de endpoints, enviar el token en el header:
```
Authorization: Bearer <token>
```

### Usuario administrador por defecto
Al iniciar la aplicación por primera vez (si no existe), se crea automáticamente:
- usuario: `admin`
- contraseña: `admin123`
- rol: `ADMIN`

**Cambia esta contraseña en producción.**

### Reglas de autorización por endpoint
| Recurso | GET | POST / PUT / DELETE |
|---|---|---|
| `/api/tipos-habitacion`, `/api/habitaciones` | Cualquier usuario autenticado | Solo `ADMIN` |
| `/api/huespedes` | Solo `ADMIN` | Solo `ADMIN` |
| `/api/reservas`, `/api/detalles-reserva` | `ADMIN` (todas) / `HUESPED` (solo las propias) | `ADMIN` (todas) / `HUESPED` (solo las propias) |

## Endpoints disponibles
| Recurso | Ruta base |
|---|---|
| Autenticación | `/api/auth` |
| Tipos de habitación | `/api/tipos-habitacion` |
| Habitaciones | `/api/habitaciones` |
| Huéspedes | `/api/huespedes` |
| Reservas | `/api/reservas` |
| Detalle de reserva | `/api/detalles-reserva` |

## Lógica de negocio destacada
Al registrar una reserva, el sistema:
1. Verifica que la fecha de salida sea posterior a la fecha de ingreso
2. Verifica que cada habitación esté disponible
3. Calcula automáticamente el precio unitario y subtotal por habitación
4. Determina el total a pagar sumando todos los subtotales
5. Actualiza la disponibilidad de cada habitación a no disponible

Al eliminar una reserva, la disponibilidad de las habitaciones asociadas es restaurada.

## Configuración (application.properties)
```properties
server.port=9090
spring.datasource.url=jdbc:mysql://localhost:3306/hotelreservas_bd
spring.datasource.username=root
spring.datasource.password=

app.jwt.secret=<clave secreta de al menos 256 bits>
app.jwt.expiration-ms=86400000
```

## Ejecución
```bash
./mvnw spring-boot:run
```
Requisitos previos:
- Java 21 instalado
- MySQL corriendo en `localhost:3306` con la base de datos `hotelreservas_bd` creada
  (o ajustar `spring.datasource.*` en `application.properties`)
