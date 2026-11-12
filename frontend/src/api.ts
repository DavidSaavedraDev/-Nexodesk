import axios from 'axios'

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('nexodesk_token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !window.location.pathname.startsWith('/login')) {
      localStorage.removeItem('nexodesk_token')
      localStorage.removeItem('nexodesk_user')
      window.location.assign('/login')
    }
    return Promise.reject(error)
  },
)

export function getErrorMessage(error: unknown, fallback = 'No se pudo completar la operación') {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data
    if (typeof data?.message === 'string') {
      if (data.fields && typeof data.fields === 'object') {
        const fields = Object.entries(data.fields as Record<string, unknown>)
          .map(([field, message]) => `${field}: ${String(message)}`)
          .join(' · ')
        return fields ? `${data.message}: ${fields}` : data.message
      }
      return data.message
    }
    if (error.response?.status === 409) return 'El correo electrónico ya está registrado'
    if (error.response?.status === 0 || !error.response) return 'No se pudo conectar con el backend'
    return `La solicitud fue rechazada (HTTP ${error.response.status})`
  }
  return fallback
}
