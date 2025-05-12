import React, { createContext, useState, useEffect, ReactNode } from 'react'
import Cookies from 'js-cookie'

type AuthContextType = {
  isAuthenticated: boolean
  login: (token: string) => void
  logout: () => void
}

export const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  login: () => {},
  logout: () => {},
})

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  useEffect(() => {
    const token = Cookies.get('accessToken')
    if (token) setIsAuthenticated(true)
  }, [])

  const login = (token: string) => {
    Cookies.set('accessToken', token)
    setIsAuthenticated(true)
  }

  const logout = () => {
    Cookies.remove('accessToken')
    setIsAuthenticated(false)
  }

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}
