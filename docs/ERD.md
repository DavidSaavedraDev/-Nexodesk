# Modelo entidad-relación inicial

El siguiente modelo representa la base funcional inicial de NEXODESK. Los nombres de estados y tipos se implementarán como enums controlados por el backend.

```mermaid
erDiagram
    USERS ||--o{ USER_ROLES : has
    ROLES ||--o{ USER_ROLES : grants
    USERS ||--o{ TICKETS : requests
    USERS ||--o{ TICKETS : assigned_to
    TICKET_CATEGORIES ||--o{ TICKETS : classifies
    SLA_POLICIES ||--o{ TICKETS : governs
    TICKETS ||--o{ TICKET_COMMENTS : contains
    USERS ||--o{ TICKET_COMMENTS : writes
    TICKETS ||--o{ TICKET_HISTORY : tracks
    USERS ||--o{ TICKET_HISTORY : changes
    USERS ||--o{ PASSWORD_RESET_TOKENS : requests
    ASSETS ||--o{ ASSET_ASSIGNMENTS : assigned
    USERS ||--o{ ASSET_ASSIGNMENTS : receives
    KNOWLEDGE_CATEGORIES ||--o{ KNOWLEDGE_ARTICLES : groups
    USERS ||--o{ KNOWLEDGE_ARTICLES : authors
    USERS ||--o{ AUDIT_LOGS : performs

    USERS {
        uuid id PK
        varchar email UK
        varchar password_hash
        boolean active
        timestamp created_at
        timestamp updated_at
    }
    ROLES {
        bigint id PK
        varchar name UK
    }
    USER_ROLES {
        uuid user_id PK, FK
        bigint role_id PK, FK
    }
    TICKETS {
        uuid id PK
        varchar ticket_number UK
        varchar title
        text description
        varchar status
        varchar priority
        uuid requester_id FK
        uuid assigned_technician_id FK
        bigint category_id FK
        bigint sla_policy_id FK
        timestamp due_at
        timestamp resolved_at
        timestamp created_at
        timestamp updated_at
    }
    TICKET_CATEGORIES {
        bigint id PK
        varchar name UK
        boolean active
    }
    SLA_POLICIES {
        bigint id PK
        varchar name
        varchar priority
        integer response_time_minutes
        integer resolution_time_minutes
        boolean active
    }
    TICKET_COMMENTS {
        uuid id PK
        uuid ticket_id FK
        uuid author_id FK
        text content
        timestamp created_at
        timestamp updated_at
    }
    TICKET_HISTORY {
        uuid id PK
        uuid ticket_id FK
        uuid changed_by FK
        varchar field_name
        text old_value
        text new_value
        timestamp created_at
    }
    PASSWORD_RESET_TOKENS {
        uuid id PK
        uuid user_id FK
        varchar token_hash
        timestamp expires_at
        timestamp used_at
    }
    ASSETS {
        uuid id PK
        varchar asset_tag UK
        varchar asset_type
        varchar serial_number UK
        varchar brand
        varchar model
        varchar status
        timestamp created_at
        timestamp updated_at
    }
    ASSET_ASSIGNMENTS {
        uuid id PK
        uuid asset_id FK
        uuid user_id FK
        timestamp assigned_at
        timestamp returned_at
    }
    KNOWLEDGE_CATEGORIES {
        bigint id PK
        varchar name UK
    }
    KNOWLEDGE_ARTICLES {
        uuid id PK
        varchar title
        varchar slug UK
        text content
        varchar status
        bigint category_id FK
        uuid author_id FK
        timestamp published_at
        timestamp created_at
        timestamp updated_at
    }
    AUDIT_LOGS {
        uuid id PK
        uuid user_id FK
        varchar action
        varchar entity_type
        uuid entity_id
        jsonb old_values
        jsonb new_values
        varchar ip_address
        timestamp created_at
    }
```

## Índices iniciales

- `users.email`
- `tickets.ticket_number`
- `tickets.status`
- `tickets.priority`
- `tickets.created_at`
- `tickets.assigned_technician_id`
- `ticket_comments.ticket_id`
- `ticket_history.ticket_id`
- `assets.serial_number`
- `knowledge_articles.slug`
- `audit_logs.user_id`
- `audit_logs.created_at`

Todas las tablas y relaciones se crearán mediante migraciones Flyway. El diseño podrá evolucionar durante la implementación, pero los cambios deberán quedar versionados y revisados.
