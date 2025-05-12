import React, { useContext, JSX } from 'react'
import { Navigate } from 'react-router-dom'
import { AuthContext } from './AuthContext'

const PrivateRoute = ({ children }: { children: JSX.Element }) => {
  const { isAuthenticated } = useContext(AuthContext)
  return isAuthenticated ? children : <Navigate to="/login" replace />
}

export default PrivateRoute
