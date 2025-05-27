import React, { createContext, useState, useEffect, ReactNode } from 'react'
import axios from 'axios'

type AuthContextType = {
  isAuthenticated: boolean
  setAuthenticated: (value: boolean) => void
  logout: () => void
}

export const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  setAuthenticated: () => {},
  logout: () => {},
})

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  useEffect(() => {
    const checkAuth = async () => {
      const token = localStorage.getItem('token')

      try {
        if (token) {
          await axios.get('http://localhost:8080/auth/me', {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
        } else {
          await axios.get('http://localhost:8080/auth/me', { withCredentials: true })
        }
        setIsAuthenticated(true)
      } catch {
        setIsAuthenticated(false)
      }
    }

    checkAuth()
  }, [])

  const logout = async () => {
    localStorage.removeItem('token') 

    try {
      await axios.post('http://localhost:8080/auth/logout', {}, { withCredentials: true })
    } catch (error) {
      console.error('Błąd podczas wylogowywania', error)
    } finally {
      setIsAuthenticated(false)
    }
  }

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated,
        setAuthenticated: setIsAuthenticated,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}
