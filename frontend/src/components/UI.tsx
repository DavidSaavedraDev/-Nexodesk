import type { ButtonHTMLAttributes, InputHTMLAttributes, ReactNode, SelectHTMLAttributes } from 'react'
import { LoaderCircle } from 'lucide-react'
import type { TicketPriority, TicketStatus } from '../types'

export function Button({ children, loading, variant = 'primary', ...props }: ButtonHTMLAttributes<HTMLButtonElement> & { loading?: boolean; variant?: 'primary' | 'ghost' | 'danger' }) {
  return <button {...props} className={`inline-flex items-center justify-center gap-2 rounded-xl px-4 py-2.5 text-sm font-semibold transition disabled:cursor-not-allowed disabled:opacity-50 ${variant === 'primary' ? 'bg-cyan-500 text-slate-950 shadow-lg shadow-cyan-500/20 hover:bg-cyan-400' : variant === 'danger' ? 'bg-red-50 text-red-600 hover:bg-red-100 dark:bg-red-500/10 dark:text-red-300' : 'text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800'} ${props.className || ''}`} disabled={loading || props.disabled}>{loading && <LoaderCircle size={16} className="animate-spin" />}{children}</button>
}
export function Field({ label, ...props }: InputHTMLAttributes<HTMLInputElement> & { label: string }) {
  return <label className="block space-y-2"><span className="text-sm font-medium text-slate-600 dark:text-slate-300">{label}</span><input {...props} className={`w-full rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm outline-none transition placeholder:text-slate-400 focus:border-cyan-400 focus:ring-4 focus:ring-cyan-400/10 dark:border-slate-700 dark:bg-slate-900/60 ${props.className || ''}`} /></label>
}
export function SelectField({ label, children, ...props }: SelectHTMLAttributes<HTMLSelectElement> & { label: string; children: ReactNode }) {
  return <label className="block space-y-2"><span className="text-sm font-medium text-slate-600 dark:text-slate-300">{label}</span><select {...props} className="w-full rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm outline-none focus:border-cyan-400 dark:border-slate-700 dark:bg-slate-900/60">{children}</select></label>
}
export function Badge({ type, value }: { type: 'priority' | 'status'; value: TicketPriority | TicketStatus }) {
  const styles: Record<string, string> = { CRITICAL: 'bg-red-50 text-red-600 dark:bg-red-500/10 dark:text-red-300', HIGH: 'bg-orange-50 text-orange-600 dark:bg-orange-500/10 dark:text-orange-300', MEDIUM: 'bg-amber-50 text-amber-600 dark:bg-amber-500/10 dark:text-amber-300', LOW: 'bg-slate-100 text-slate-600 dark:bg-slate-700 dark:text-slate-300', OPEN: 'bg-blue-50 text-blue-600 dark:bg-blue-500/10 dark:text-blue-300', IN_PROGRESS: 'bg-cyan-50 text-cyan-700 dark:bg-cyan-500/10 dark:text-cyan-300', PENDING: 'bg-violet-50 text-violet-600 dark:bg-violet-500/10 dark:text-violet-300', CLOSED: 'bg-emerald-50 text-emerald-600 dark:bg-emerald-500/10 dark:text-emerald-300' }
  return <span className={`inline-flex rounded-full px-2.5 py-1 text-[11px] font-bold ${styles[value]}`}>{value.replace('_', ' ')}</span>
}
export function Card({ children, className = '' }: { children: ReactNode; className?: string }) { return <section className={`rounded-2xl border border-slate-200 bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-[#0d1a2c] ${className}`}>{children}</section> }
export function ErrorMessage({ message }: { message: string }) { return message ? <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-600 dark:border-red-500/20 dark:bg-red-500/10 dark:text-red-300">{message}</div> : null }
