# API de tickets

La documentación interactiva se publica en `/swagger-ui/index.html` cuando el backend está en ejecución. La especificación OpenAPI está disponible en `/v3/api-docs`.

## Autenticación

Registrar un empleado:

```http
POST /api/auth/register
Content-Type: application/json

{
  "firstName": "Ana",
  "lastName": "Pérez",
  "email": "ana@example.com",
  "password": "una-clave-segura"
}
```

Iniciar sesión:

```http
POST /api/auth/login
Content-Type: application/json
```

Usar el `token` devuelto como:

```http
Authorization: Bearer <token>
```

## Endpoints

| Método | Ruta | Acceso |
|---|---|---|
| `POST` | `/api/tickets` | Usuario autenticado |
| `GET` | `/api/tickets` | ADMIN/TECHNICIAN: todos; EMPLOYEE: propios |
| `GET` | `/api/tickets/{id}` | Solicitante, técnico asignado o ADMIN |
| `PUT` | `/api/tickets/{id}` | Según rol y asignación |
| `POST` | `/api/tickets/{id}/assign` | ADMIN/TECHNICIAN |
| `POST` | `/api/tickets/{id}/comments` | Usuarios con acceso al ticket |
| `GET` | `/api/tickets/{id}/comments` | Usuarios con acceso al ticket |
| `GET` | `/api/tickets/{id}/history` | Usuarios con permiso de gestión |

## Valores controlados

Prioridades: `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`.

Estados: `OPEN`, `IN_PROGRESS`, `PENDING`, `CLOSED`.

Los cambios de título, descripción, prioridad, estado y asignación se registran en el historial.
