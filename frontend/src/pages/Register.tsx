import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import AuthLayout from './AuthLayout'
import { Button, ErrorMessage, Field } from '../components/UI'
import { getErrorMessage } from '../api'
import { useAuth } from '../context/AuthContext'

export default function Register() {
  const { register } = useAuth(); const navigate = useNavigate(); const [form, setForm] = useState({ firstName: '', lastName: '', email: '', password: '' }); const [error, setError] = useState(''); const [loading, setLoading] = useState(false)
  const change = (key: keyof typeof form) => (e: React.ChangeEvent<HTMLInputElement>) => setForm({ ...form, [key]: e.target.value })
  async function submit(e: React.FormEvent) { e.preventDefault(); setError(''); setLoading(true); try { await register(form); navigate('/') } catch (err) { setError(getErrorMessage(err, 'No se pudo crear la cuenta')) } finally { setLoading(false) } }
  return <AuthLayout title="Create your account" subtitle="Start managing support with clarity."><form onSubmit={submit} className="space-y-4"><ErrorMessage message={error} /><div className="grid gap-4 sm:grid-cols-2"><Field label="Nombre" required value={form.firstName} onChange={change('firstName')} /><Field label="Apellido" required value={form.lastName} onChange={change('lastName')} /></div><Field label="Correo electrónico" type="email" required value={form.email} onChange={change('email')} /><Field label="Contraseña" type="password" minLength={8} required value={form.password} onChange={change('password')} /><Button type="submit" loading={loading} className="mt-3 w-full">Crear cuenta</Button><p className="text-center text-sm text-slate-500">¿Ya tienes cuenta? <Link to="/login" className="font-semibold text-cyan-600">Iniciar sesión</Link></p></form></AuthLayout>
}
