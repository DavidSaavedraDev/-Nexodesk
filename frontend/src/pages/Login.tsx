import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import AuthLayout from './AuthLayout'
import { Button, ErrorMessage, Field } from '../components/UI'
import { getErrorMessage } from '../api'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const { login } = useAuth(); const navigate = useNavigate(); const location = useLocation()
  const [email, setEmail] = useState(''); const [password, setPassword] = useState(''); const [error, setError] = useState(''); const [loading, setLoading] = useState(false)
  async function submit(event: React.FormEvent) { event.preventDefault(); setError(''); setLoading(true); try { await login(email, password); navigate((location.state as { from?: { pathname: string } })?.from?.pathname || '/') } catch (e) { setError(getErrorMessage(e, 'Correo o contraseña incorrectos')) } finally { setLoading(false) } }
  return <AuthLayout title="Welcome back" subtitle="Sign in to your operations workspace."><form onSubmit={submit} className="space-y-5"><ErrorMessage message={error} /><Field label="Correo electrónico" type="email" required value={email} onChange={e => setEmail(e.target.value)} placeholder="you@company.com" /><Field label="Contraseña" type="password" required value={password} onChange={e => setPassword(e.target.value)} placeholder="••••••••" /><Button type="submit" loading={loading} className="mt-2 w-full">Iniciar sesión</Button><p className="text-center text-sm text-slate-500">¿Aún no tienes cuenta? <Link to="/register" className="font-semibold text-cyan-600 hover:text-cyan-500">Crear cuenta</Link></p></form></AuthLayout>
}
