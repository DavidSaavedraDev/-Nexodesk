import { createContext, useContext, useMemo, useState, type ReactNode } from 'react'
import { api } from '../api'
import type { AuthResponse, Role } from '../types'

interface AuthContextValue {
  auth: AuthResponse | null
  login: (email: string, password: string) => Promise<void>
  register: (data: { firstName: string; lastName: string; email: string; password: string }) => Promise<void>
  logout: () => void
  hasRole: (...roles: Role[]) => boolean
}

const AuthContext = createContext<AuthContextValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [auth, setAuth] = useState<AuthResponse | null>(() => {
    const stored = localStorage.getItem('nexodesk_user')
    return stored ? JSON.parse(stored) : null
  })

  const save = (data: AuthResponse) => {
    localStorage.setItem('nexodesk_token', data.token)
    localStorage.setItem('nexodesk_user', JSON.stringify(data))
    setAuth(data)
  }

  const value = useMemo<AuthContextValue>(() => ({
    auth,
    async login(email, password) {
      const { data } = await api.post<AuthResponse>('/auth/login', { email, password })
      save(data)
    },
    async register(payload) {
      const { data } = await api.post<AuthResponse>('/auth/register', payload)
      save(data)
    },
    logout() {
      localStorage.removeItem('nexodesk_token')
      localStorage.removeItem('nexodesk_user')
      setAuth(null)
    },
    hasRole: (...roles) => auth !== null && roles.includes(auth.role),
  }), [auth])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth must be used within AuthProvider')
  return context
}
