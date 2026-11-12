# Roles y permisos

## Roles

| Rol | Propósito |
|---|---|
| `ADMIN` | Administración global de la plataforma, usuarios, configuración y auditoría. |
| `TECHNICIAN` | Atención y resolución de solicitudes e incidencias asignadas. |
| `EMPLOYEE` | Creación y seguimiento de sus propias solicitudes y consulta de conocimiento. |

## Matriz inicial

| Recurso / acción | ADMIN | TECHNICIAN | EMPLOYEE |
|---|:---:|:---:|:---:|
| Consultar dashboard | Sí | Sí | Sí |
| Crear ticket | Sí | Sí | Sí |
| Consultar tickets propios | Sí | Sí | Sí |
| Consultar todos los tickets | Sí | Sí | No |
| Editar ticket propio | Sí | Sí | Limitado |
| Asignar técnicos | Sí | Sí | No |
| Cambiar estado de ticket | Sí | Sí | No |
| Comentar tickets | Sí | Sí | Sí |
| Administrar categorías y SLA | Sí | No | No |
| Consultar inventario | Sí | Sí | Sí |
| Administrar inventario | Sí | Limitado | No |
| Administrar usuarios y roles | Sí | No | No |
| Desactivar usuarios | Sí | No | No |
| Consultar artículos publicados | Sí | Sí | Sí |
| Crear y editar artículos | Sí | Sí | No |
| Publicar artículos | Sí | Limitado | No |
| Consultar reportes | Sí | Sí | No |
| Exportar reportes | Sí | Sí | No |
| Consultar auditoría | Sí | No | No |

## Reglas de autorización

1. El backend será la autoridad final de autorización; ocultar elementos en el frontend no constituye seguridad.
2. Un usuario desactivado no podrá iniciar sesión ni utilizar tokens nuevos.
3. Un empleado solo podrá consultar tickets en los que sea solicitante, salvo permisos explícitos futuros.
4. Los técnicos podrán operar tickets según su asignación y los permisos definidos por el módulo.
5. Las acciones administrativas y los cambios relevantes quedarán registrados en auditoría.
6. Cada endpoint documentará explícitamente los roles autorizados.

La matriz podrá ampliarse con permisos más granulares cuando se implemente el módulo de autorización.
