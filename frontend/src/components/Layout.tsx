import { useState } from 'react'
import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { BarChart3, ClipboardList, Home, LogOut, Menu, Moon, Plus, Sun, UserCircle, Users, X } from 'lucide-react'
import { useAuth } from '../context/AuthContext'

const links = [
  { to: '/', label: 'Dashboard', icon: Home },
  { to: '/tickets', label: 'Tickets', icon: ClipboardList },
  { to: '/tickets/new', label: 'Nuevo ticket', icon: Plus },
  { to: '/users', label: 'Usuarios y técnicos', icon: Users, roles: ['ADMIN', 'TECHNICIAN'] as const },
  { to: '/profile', label: 'Mi perfil', icon: UserCircle },
]

export default function Layout() {
  const { auth, logout, hasRole } = useAuth()
  const navigate = useNavigate()
  const [open, setOpen] = useState(false)
  const [dark, setDark] = useState(() => localStorage.getItem('nexodesk_theme') === 'dark')
  const toggleTheme = () => {
    const next = !dark
    setDark(next)
    document.documentElement.classList.toggle('dark', next)
    localStorage.setItem('nexodesk_theme', next ? 'dark' : 'light')
  }
  const signOut = () => { logout(); navigate('/login') }

  return (
    <div className="min-h-screen bg-[#f5f7fb] text-slate-800 dark:bg-[#08111f] dark:text-slate-100">
      {open && <button aria-label="Cerrar menú" onClick={() => setOpen(false)} className="fixed inset-0 z-30 bg-slate-950/40 lg:hidden" />}
      <aside className={`fixed inset-y-0 left-0 z-40 flex w-72 flex-col border-r border-slate-200 bg-white px-5 py-6 transition-transform dark:border-slate-800 dark:bg-[#0d1a2c] lg:translate-x-0 ${open ? 'translate-x-0' : '-translate-x-full'}`}>
        <div className="mb-10 flex items-center justify-between px-2">
          <div className="flex items-center gap-3"><div className="grid h-10 w-10 place-items-center rounded-xl bg-cyan-500 font-black text-slate-950">N</div><div><div className="text-lg font-bold tracking-tight">NEXODESK</div><div className="text-[10px] uppercase tracking-[.25em] text-slate-400">ITSM platform</div></div></div>
          <button onClick={() => setOpen(false)} className="text-slate-400 lg:hidden"><X size={20} /></button>
        </div>
        <div className="mb-3 px-3 text-[11px] font-semibold uppercase tracking-widest text-slate-400">Workspace</div>
        <nav className="space-y-1">
          {links.filter(link => !link.roles || hasRole(...link.roles)).map(({ to, label, icon: Icon }) => (
            <NavLink key={to} to={to} end={to === '/'} onClick={() => setOpen(false)} className={({ isActive }) => `flex items-center gap-3 rounded-xl px-3 py-3 text-sm font-medium transition ${isActive ? 'bg-cyan-50 text-cyan-700 dark:bg-cyan-400/10 dark:text-cyan-300' : 'text-slate-500 hover:bg-slate-50 hover:text-slate-900 dark:text-slate-400 dark:hover:bg-slate-800 dark:hover:text-white'}`}><Icon size={18} />{label}</NavLink>
          ))}
        </nav>
        <div className="mt-auto rounded-2xl bg-slate-50 p-4 dark:bg-slate-800/60">
          <div className="mb-3 flex items-center gap-3"><div className="grid h-9 w-9 place-items-center rounded-full bg-cyan-100 text-sm font-bold text-cyan-700 dark:bg-cyan-400/20 dark:text-cyan-300">{auth?.email[0].toUpperCase()}</div><div className="min-w-0"><div className="truncate text-sm font-semibold">{auth?.email}</div><div className="text-xs text-slate-400">{auth?.role}</div></div></div>
          <button onClick={signOut} className="flex w-full items-center gap-2 rounded-lg px-1 py-2 text-sm text-slate-500 hover:text-red-500"><LogOut size={16} />Cerrar sesión</button>
        </div>
      </aside>
      <div className="lg:pl-72">
        <header className="sticky top-0 z-20 flex h-20 items-center justify-between border-b border-slate-200 bg-white/80 px-5 backdrop-blur-xl dark:border-slate-800 dark:bg-[#08111f]/80 sm:px-8">
          <button onClick={() => setOpen(true)} className="text-slate-500 lg:hidden"><Menu /></button>
          <div className="hidden text-sm text-slate-400 sm:block">Operations center <span className="mx-2">/</span> <span className="text-slate-700 dark:text-slate-200">Support workspace</span></div>
          <div className="ml-auto flex items-center gap-3"><button onClick={toggleTheme} className="rounded-xl p-2.5 text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800">{dark ? <Sun size={18} /> : <Moon size={18} />}</button><div className="h-8 w-px bg-slate-200 dark:bg-slate-800" /><div className="hidden text-right sm:block"><div className="text-sm font-semibold">{auth?.email}</div><div className="text-xs text-slate-400">{auth?.role}</div></div></div>
        </header>
        <main className="mx-auto max-w-[1440px] p-5 sm:p-8"><Outlet /></main>
      </div>
    </div>
  )
}
