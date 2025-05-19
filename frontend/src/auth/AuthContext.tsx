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
  axios
    .get('http://localhost:8080/auth/me', { withCredentials: true })
    .then(() => {
      //console.log('Ustawiam isAuthenticated na true');
      setIsAuthenticated(true);
    })
    .catch(() => {
      //console.log('Ustawiam isAuthenticated na false');
      setIsAuthenticated(false);
    });
}, []);

//console.log('Aktualny isAuthenticated:', isAuthenticated);


  const logout = () => {
    axios
      .post('http://localhost:8080/auth/logout', {}, { withCredentials: true })
      .finally(() => setIsAuthenticated(false))
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
