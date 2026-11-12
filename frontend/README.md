# NEXODESK Frontend

React + TypeScript + Vite frontend for the NEXODESK ITSM API.

## Requirements

- Node.js 20+.
- NEXODESK backend running on `http://localhost:8080`.

## Setup

From this directory:

```powershell
npm install
Copy-Item .env.example .env
npm run dev
```

The app runs at `http://localhost:5173`.

`VITE_API_URL` defaults to `http://localhost:8080/api`. Change it in `.env` only when the backend uses another URL. Do not put credentials or JWT secrets in frontend environment files.

## Production build

```powershell
npm run build
npm run preview
```

## Implemented API flows

- Employee registration: `POST /api/auth/register`.
- Login and JWT session: `POST /api/auth/login`.
- Ticket creation, listing, detail and updates.
- Ticket comments and history.
- Route protection based on the authenticated role.
- Unauthorized sessions are cleared when the API returns `401`.

The users page intentionally does not show fake records because the current backend does not expose user-management endpoints.
