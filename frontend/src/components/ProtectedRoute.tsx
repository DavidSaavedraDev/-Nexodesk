import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import type { Role } from '../types'

export default function ProtectedRoute({ roles }: { roles?: Role[] }) {
  const { auth, hasRole } = useAuth()
  const location = useLocation()
  if (!auth) return <Navigate to="/login" state={{ from: location }} replace />
  if (roles && !hasRole(...roles)) return <Navigate to="/denied" replace />
  return <Outlet />
}
