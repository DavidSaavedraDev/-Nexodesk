export type Role = 'ADMIN' | 'TECHNICIAN' | 'EMPLOYEE'
export type TicketPriority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
export type TicketStatus = 'OPEN' | 'IN_PROGRESS' | 'PENDING' | 'CLOSED'

export interface AuthResponse {
  token: string
  userId: string
  email: string
  role: Role
}

export interface Ticket {
  id: string
  ticketNumber: string
  title: string
  description: string
  priority: TicketPriority
  status: TicketStatus
  requesterId: string
  assignedTechnicianId: string | null
  createdAt: string
  updatedAt: string
}

export interface Comment {
  id: string
  authorId: string
  content: string
  createdAt: string
}

export interface HistoryItem {
  id: string
  changedById: string
  fieldName: string
  oldValue: string | null
  newValue: string | null
  createdAt: string
}
