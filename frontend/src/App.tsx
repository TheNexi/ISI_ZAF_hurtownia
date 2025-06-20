import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Home from './pages/Home'
import Orders from './pages/Orders'
import Profile from './pages/Profile'
import Products from './pages/Products'
import AdminHome from './admin/pages/Home'
import AdminOrders from './admin/pages/Orders'
import AdminProducts from './admin/pages/Products'
import { AuthProvider } from './auth/AuthContext'
import PrivateRoute from './auth/PrivateRoute'
import './styles/global.css'
import SuccessPage from './pages/Orders/SuccessPage'

const App = () => {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  )
}

const AppRoutes = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/products" element={<Products />} />
        <Route path="/" element={<Home />} />

        <Route
          path="/orders"
          element={
            <PrivateRoute requiredRole="USER">
              <Orders />
            </PrivateRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <PrivateRoute requiredRole="USER">
              <Profile />
            </PrivateRoute>
          }
        />

        <Route
          path="/platnosc/sukces"
          element={
            <PrivateRoute requiredRole="USER">
              <SuccessPage />
            </PrivateRoute>
          }
        />

        <Route
          path="/admin"
          element={
            <PrivateRoute requiredRole="ADMIN">
              <AdminHome />
            </PrivateRoute>
          }
        />
        <Route
          path="/admin/orders"
          element={
            <PrivateRoute requiredRole="ADMIN">
              <AdminOrders />
            </PrivateRoute>
          }
        />
        <Route
          path="/admin/products"
          element={
            <PrivateRoute requiredRole="ADMIN">
              <AdminProducts />
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  )
}

export default App
