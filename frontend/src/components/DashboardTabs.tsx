import React, { useContext } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { AuthContext } from '../auth/AuthContext'

interface DashboardTabsProps {
  role: 'USER' | 'ADMIN'
}

const DashboardTabs: React.FC<DashboardTabsProps> = ({ role }) => {
  const { logout } = useContext(AuthContext)
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="header">
      <nav className="dashboard-tabs">
        {role === 'ADMIN' ? (
          <>
            <NavLink to="/admin" end className={({ isActive }) => isActive ? 'tab active' : 'tab'}>
              Panel główny
            </NavLink>
            <NavLink to="/admin/orders" className={({ isActive }) => isActive ? 'tab active' : 'tab'}>
              Zamówienia
            </NavLink>
            <NavLink to="/admin/products" className={({ isActive }) => isActive ? 'tab active' : 'tab'}>
              Produkty
            </NavLink>
            <NavLink to="/admin/deliveries" className={({ isActive }) => isActive ? 'tab active' : 'tab'}>
              Dostawy
            </NavLink>
          </>
        ) : (
          <>
            <NavLink to="/" end className={({ isActive }) => isActive ? 'tab active' : 'tab'}>
              Panel główny
            </NavLink>
            <NavLink to="/orders" className={({ isActive }) => isActive ? 'tab active' : 'tab'}>
              Zamówienia
            </NavLink>
            <NavLink to="/products" className={({ isActive }) => isActive ? 'tab active' : 'tab'}>
              Produkty
            </NavLink>
            <NavLink to="/deliveries" className={({ isActive }) => isActive ? 'tab active' : 'tab'}>
              Dostawy
            </NavLink>
          </>
        )}
      </nav>
      <button className="btn btn-logout" onClick={handleLogout}>
        Wyloguj się
      </button>
    </div>
  )
}

export default DashboardTabs
