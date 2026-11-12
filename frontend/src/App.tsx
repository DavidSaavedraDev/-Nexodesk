import { Navigate, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout'
import ProtectedRoute from './components/ProtectedRoute'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import Tickets from './pages/Tickets'
import TicketForm from './pages/TicketForm'
import TicketDetail from './pages/TicketDetail'
import Profile from './pages/Profile'
import Users from './pages/Users'
import Denied from './pages/Denied'

export default function App() {
  return <Routes>
    <Route path="/login" element={<Login />} />
    <Route path="/register" element={<Register />} />
    <Route path="/denied" element={<Denied />} />
    <Route element={<ProtectedRoute />}><Route element={<Layout />}>
      <Route index element={<Dashboard />} />
      <Route path="/tickets" element={<Tickets />} />
      <Route path="/tickets/new" element={<TicketForm />} />
      <Route path="/tickets/:id" element={<TicketDetail />} />
      <Route path="/profile" element={<Profile />} />
      <Route element={<ProtectedRoute roles={['ADMIN', 'TECHNICIAN']} />}><Route path="/users" element={<Users />} /></Route>
    </Route></Route>
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes>
}
