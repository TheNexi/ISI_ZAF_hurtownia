import React, { useContext } from 'react'
import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Home from './pages/Home'
import Orders from './pages/Orders'
import Deliveries from './pages/Deliveries'
import Products from './pages/Products'
import AdminHome from './admin/pages/Home'
import AdminOrders from './admin/pages//Orders'
import AdminProducts from './admin/pages//Products'
import AdminDeliveries from './admin/pages/Deliveries'
import { AuthProvider, AuthContext } from './auth/AuthContext'
import PrivateRoute from './auth/PrivateRoute'
import './styles/global.css'

const App = () => {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  )
}

const AppRoutes = () => {
  const { user } = useContext(AuthContext)

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route
          path="/"
          element={
            <PrivateRoute>
              <Home />
            </PrivateRoute>
          }
        />
        <Route
          path="/orders"
          element={
            <PrivateRoute>
              <Orders />
            </PrivateRoute>
          }
        />
        <Route
          path="/products"
          element={
            <PrivateRoute>
              <Products />
            </PrivateRoute>
          }
        />
        <Route
          path="/deliveries"
          element={
            <PrivateRoute>
              <Deliveries />
            </PrivateRoute>
          }
        />

        <Route
          path="/admin"
          element={
            <PrivateRoute>
              {user?.role === 'ADMIN' ? <AdminHome /> : <div>Brak dostępu</div>}
            </PrivateRoute>
          }
        />
        <Route
          path="/admin/orders"
          element={
            <PrivateRoute>
              {user?.role === 'ADMIN' ? <AdminOrders /> : <div>Brak dostępu</div>}
            </PrivateRoute>
          }
        />
        <Route
          path="/admin/products"
          element={
            <PrivateRoute>
              {user?.role === 'ADMIN' ? <AdminProducts /> : <div>Brak dostępu</div>}
            </PrivateRoute>
          }
        />
        <Route
          path="/admin/deliveries"
          element={
            <PrivateRoute>
              {user?.role === 'ADMIN' ? <AdminDeliveries /> : <div>Brak dostępu</div>}
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App
