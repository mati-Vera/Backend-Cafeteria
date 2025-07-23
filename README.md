# Backend Cafeteria - Spring Boot + Auth0

Este proyecto es el backend de una aplicación de cafetería, desarrollado con Spring Boot. Permite la gestión de menú y pedidos, implementa autenticación y autorización basada en Auth0, y define roles de acceso para distintos tipos de usuario.

## Características

- Spring Boot 3.5.3
- Spring Security con OAuth2 y Auth0
- Gestión de menú y pedidos
- Roles: ADMIN, BARISTA, CLIENTE
- Base de datos MySQL
- Manejo global de excepciones

## Base de Datos

El sistema utiliza MySQL como base de datos relacional. La configuración de conexión (URL, usuario, contraseña) se encuentra en el archivo `src/main/resources/application.properties`. El esquema se actualiza automáticamente mediante JPA/Hibernate.

## Instrucciones de Ejecución

1. Configura la base de datos MySQL y actualiza las credenciales en `application.properties`.
2. Instala las dependencias con Maven.
3. Ejecuta la aplicación con Maven.

El backend se ejecuta por defecto en `http://localhost:8080`.

## Endpoints Principales

### Públicos (no requieren autenticación)

- `GET /public` — Información general pública
- `GET /public/menu` — Consulta el menú disponible
- `GET /api/public/health` — Health check del servicio

### Privados (requieren autenticación Auth0)

#### Menú (solo ADMIN)

- `GET /private/menu` — Listar todos los ítems del menú
- `POST /private/menu` — Crear un nuevo ítem de menú
- `PUT /private/menu/{id}` — Actualizar un ítem de menú
- `DELETE /private/menu/{id}` — Eliminar un ítem de menú

#### Pedidos

- `POST /private/orders` — Crear pedido (CLIENTE)
- `GET /private/orders/my` — Ver pedidos propios (CLIENTE)
- `GET /private/orders` — Ver todos los pedidos (BARISTA, ADMIN)
- `PUT /private/orders/{id}/status` — Actualizar estado de pedido (BARISTA, ADMIN)

#### Otros

- `GET /private` — Ruta privada general (cualquier usuario autenticado)
- `GET /private/profile` — Perfil del usuario autenticado

## Roles y Permisos

- **ADMIN:** Acceso total a la gestión de menú y pedidos.
- **BARISTA:** Puede ver y actualizar el estado de todos los pedidos.
- **CLIENTE:** Puede crear pedidos y ver solo sus propios pedidos. Acceso de solo lectura al menú público.

## Notas

- La autenticación y autorización se realiza mediante Auth0. Configura tus credenciales en `application.properties`.
- El frontend debe consumir estos endpoints usando tokens válidos de Auth0.
