# Backend Cafeteria - Spring Boot + Auth0

## Instrucciones para clonar y ejecutar el proyecto

### Requisitos previos

- Java 17 o superior
- Maven 3.8+
- MySQL (o MariaDB) corriendo localmente
- (Opcional) Postman o similar para probar la API

### Pasos para clonar y ejecutar

1. **Clona el repositorio:**

   ```sh
   git clone <URL_DEL_REPO>
   cd Backend-Cafeteria
   ```

2. **Configura la base de datos:**

   - Crea una base de datos llamada `cafeteria` en tu MySQL local.
   - Edita `src/main/resources/application.properties` y ajusta:
     - `spring.datasource.username` (usuario de MySQL)
     - `spring.datasource.password` (contraseña de MySQL)

3. **Configura Auth0:**

   - Completa los valores de Auth0 en `application.properties`:
     - `auth0.domain`, `auth0.clientId`, `auth0.clientSecret`, `auth0.audience`, `auth0.baristaRoleId`

4. **Instala dependencias y compila:**

   ```sh
   ./mvnw clean install
   ```

   o si usas Windows:

   ```sh
   mvnw.cmd clean install
   ```

5. **Ejecuta la aplicación:**

   ```sh
   ./mvnw spring-boot:run
   ```

   o en Windows:

   ```sh
   mvnw.cmd spring-boot:run
   ```

6. **La API estará disponible en:**

   - `http://localhost:8080`

7. **Prueba la API:**
   - Usa Postman, Insomnia o el frontend para probar los endpoints.

---

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

## Lógica de Sincronización de Usuarios y Roles

- Cuando un usuario accede al backend con un token JWT de Auth0, se extraen los datos básicos (ID, nombre, email) y el **rol** directamente de los claims del token.
- El rol se obtiene del claim personalizado: `https://cafeteria.com/roles` (primer valor del array).
- Si el claim de roles no existe o está vacío, se asigna el rol por defecto `CLIENTE`.
- **No se consulta la API de Auth0** para obtener ni actualizar roles o metadatos.
- La base de datos se mantiene sincronizada con los datos del usuario cada vez que accede con un token válido.

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
- `GET /api/me` — Sincroniza y devuelve los datos del usuario autenticado

### Usuarios y Baristas

- `GET /private/users?role=barista` — Listar todos los baristas (solo ADMIN). Devuelve: id, email, name, roles.
- `POST /private/users` — Crear un barista (solo ADMIN). Recibe email y password, crea el usuario en Auth0 y le asigna solo el rol BARISTA.

### Pedidos (mejoras)

- En cada ítem de pedido (`OrderItem`), la respuesta incluye el campo `menuItemName` con el nombre del producto.
- En cada pedido (`Order`), la respuesta incluye el campo `customerEmail` con el correo del cliente.

## Roles y Permisos

- **ADMIN:** Acceso total a la gestión de menú y pedidos.
- **BARISTA:** Puede ver y actualizar el estado de todos los pedidos.
- **CLIENTE:** Puede crear pedidos y ver solo sus propios pedidos. Acceso de solo lectura al menú público.

## Notas

- La autenticación y autorización se realiza mediante Auth0. El rol se extrae del claim `https://cafeteria.com/roles` del JWT.
- El backend ya no consulta la API de Auth0 para roles ni metadatos.
- El frontend debe consumir estos endpoints usando tokens válidos de Auth0.

## Integración y lógica de roles con Auth0

- Los baristas creados desde el backend se registran en Auth0 y reciben solo el rol BARISTA (no CLIENTE), gracias a la lógica de `app_metadata` y un script de post-registration en Auth0.
- El frontend puede consumir los endpoints de usuarios/baristas y pedidos de forma directa y sencilla, mostrando nombres de productos y correos de clientes sin lógica adicional.

## Sincronización de usuarios desde el frontend

Después de que un usuario inicia sesión (login/signup), el frontend debe hacer una petición autenticada a `/api/me` en el backend. Esto asegura que el usuario quede registrado y sincronizado en la base de datos local del backend antes de realizar pedidos u otras acciones privadas.

Si el usuario ya existe, simplemente se actualiza. Si es nuevo, se crea automáticamente.

> **Nota:** Esta sincronización funciona para todos los roles: cliente, barista y admin. Cualquier usuario autenticado que llame a `/api/me` será registrado o actualizado en la base local, sin importar su rol.
