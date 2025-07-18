# Backend Cafeteria - Spring Boot + Auth0

Este es un proyecto backend desarrollado con Spring Boot que implementa autenticación con Auth0.

## Características

- ✅ Spring Boot 3.5.3
- ✅ Spring Security con OAuth2
- ✅ Autenticación con Auth0
- ✅ Rutas públicas y protegidas
- ✅ CORS configurado
- ✅ Manejo de excepciones global

## Rutas Disponibles

### Rutas Públicas (sin autenticación)

- `GET /` - Página principal
- `GET /public` - Ruta pública general
- `GET /api/public` - Ruta pública de API
- `GET /api/public/health` - Health check

### Rutas Privadas (requieren token Auth0)

- `GET /private` - Ruta privada general
- `GET /private/profile` - Perfil del usuario
- `GET /api/private` - Ruta privada de API

## Configuración de Auth0

1. Crea una cuenta en [Auth0](https://auth0.com)
2. Crea una nueva aplicación (Single Page Application)
3. Configura las URLs permitidas:

   - Allowed Callback URLs: `http://localhost:3000/callback`
   - Allowed Logout URLs: `http://localhost:3000`
   - Allowed Web Origins: `http://localhost:3000`

4. Actualiza el archivo `src/main/resources/application.properties`:

```properties
# Auth0 Configuration
auth0.audience=https://your-domain.auth0.com/api/v2/
auth0.issuer=https://your-domain.auth0.com/
auth0.secret=your-auth0-secret
```

Reemplaza:

- `your-domain` con tu dominio de Auth0
- `your-auth0-secret` con el secret de tu aplicación

## Ejecutar el Proyecto

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar en modo desarrollo
mvn spring-boot:run
```

El servidor se ejecutará en `http://localhost:8080`

## Probar las APIs

### Rutas Públicas

```bash
# Health check
curl http://localhost:8080/api/public/health

# Ruta pública
curl http://localhost:8080/api/public
```

### Rutas Privadas

```bash
# Necesitas un token válido de Auth0
curl -H "Authorization: Bearer YOUR_AUTH0_TOKEN" http://localhost:8080/api/private
```

## Estructura del Proyecto

```
src/main/java/com/mati/curso/springboot/webapp/backendcafeteria/
├── BackendCafeteriaApplication.java
├── config/
│   ├── Auth0Properties.java
│   └── SecurityConfig.java
├── controllers/
│   ├── ApiController.java
│   ├── PublicController.java
│   └── PrivateController.java
└── exceptions/
    └── GlobalExceptionHandler.java
```

## Próximos Pasos

1. Configurar Auth0 con tus credenciales
2. Desarrollar el frontend que consumirá estas APIs
3. Agregar más funcionalidades específicas de la cafetería
4. Implementar base de datos y modelos de datos
