# Arquitectura de NEXODESK

## Principios

- Separación estricta entre presentación, aplicación, dominio y persistencia.
- Lógica de negocio en servicios, no en controladores ni componentes visuales.
- API REST versionada y contratos definidos mediante DTOs.
- Validación de entrada en backend y frontend.
- Migraciones reproducibles con Flyway.
- Configuración sensible mediante variables de entorno.
- Pruebas unitarias y de integración para los casos críticos.

## Vista general

```text
React + TypeScript
        │
        │ HTTPS / REST / JSON / JWT
        ▼
Spring Boot API
        │
        ├── Security + autorización por roles
        ├── Controllers
        ├── Services
        ├── Repositories
        └── Exception handling + validación
        │
        ▼
Spring Data JPA / Hibernate
        │
        ▼
PostgreSQL 16
```

## Backend

```text
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/nexodesk/
    │   │   ├── config/
    │   │   ├── security/
    │   │   ├── exception/
    │   │   ├── common/
    │   │   ├── auth/
    │   │   ├── user/
    │   │   ├── ticket/
    │   │   ├── asset/
    │   │   ├── knowledge/
    │   │   ├── report/
    │   │   └── audit/
    │   └── resources/
    │       ├── application.yml
    │       └── db/migration/
    └── test/
```

Cada dominio seguirá una estructura interna consistente:

```text
<domain>/
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
└── validation/
```

Los controladores manejarán HTTP, los servicios las reglas de negocio, los repositorios el acceso a datos y los DTOs el contrato externo de la API.

## Frontend

```text
frontend/
├── package.json
└── src/
    ├── app/
    ├── components/
    │   ├── ui/
    │   ├── layout/
    │   ├── feedback/
    │   └── charts/
    ├── context/
    ├── hooks/
    ├── layouts/
    ├── pages/
    ├── routes/
    ├── services/
    ├── types/
    └── utils/
```

Los componentes visuales no contendrán lógica de negocio. La comunicación con la API se centralizará en `services`, y el estado transversal se gestionará mediante contextos y hooks.

## Seguridad

- Contraseñas almacenadas con BCrypt.
- JWT para autenticación de la API.
- Rutas frontend protegidas.
- Autorización backend basada en roles.
- Validación de DTOs con Bean Validation.
- Manejo global de excepciones con respuestas consistentes.
- Secretos y credenciales fuera del repositorio.

## Persistencia y migraciones

PostgreSQL será la base de datos principal. Flyway gestionará todas las modificaciones de esquema mediante migraciones versionadas. Hibernate no será responsable de modificar el esquema en entornos compartidos.

## Observabilidad y calidad

- Logs estructurados para errores y acciones relevantes.
- Auditoría de operaciones sensibles.
- Swagger / OpenAPI para el contrato REST.
- JUnit y Mockito para pruebas de servicios y componentes aislados.
- Pruebas de integración para seguridad, persistencia y endpoints críticos.
