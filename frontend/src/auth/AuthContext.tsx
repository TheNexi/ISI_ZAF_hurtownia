import React, { createContext, useState, useEffect, ReactNode } from 'react'
import axios from 'axios'

type UserType = {
  login: string
  email: string
  role: string
}

type AuthContextType = {
  isAuthenticated: boolean
  setAuthenticated: (value: boolean) => void
  user: UserType | null
  setUser: (user: UserType | null) => void
  logout: () => void
}

export const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  setAuthenticated: () => {},
  user: null,
  setUser: () => {},
  logout: () => {},
})

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const [user, setUser] = useState<UserType | null>(null)

  useEffect(() => {
    axios
      .get('http://localhost:8080/auth/me', { withCredentials: true })
      .then((res) => {
        setIsAuthenticated(true)
        setUser(res.data) 
      })
      .catch(() => {
        setIsAuthenticated(false)
        setUser(null)
      })
  }, [])

  const logout = () => {
    axios
      .post('http://localhost:8080/auth/logout', {}, { withCredentials: true })
      .finally(() => {
        setIsAuthenticated(false)
        setUser(null)
      })
  }

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        setAuthenticated: setIsAuthenticated,
        user,
        setUser,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}
