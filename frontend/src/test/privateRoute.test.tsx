import React from 'react'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { MemoryRouter, Route, Routes } from 'react-router-dom'
import PrivateRoute from '../auth/PrivateRoute'
import { AuthContext } from '../auth/AuthContext'

const TestComponent = () => <div>Chroniona zawartość</div>

describe('PrivateRoute', () => {
  const renderWithAuth = (authValue: any, requiredRole?: string, initialEntries = ['/protected']) => {
    return render(
      <AuthContext.Provider value={authValue}>
        <MemoryRouter initialEntries={initialEntries}>
          <Routes>
            <Route
              path="/protected"
              element={
                <PrivateRoute requiredRole={requiredRole}>
                  <TestComponent />
                </PrivateRoute>
              }
            />
            <Route path="/login" element={<div>Strona logowania</div>} />
            <Route path="/" element={<div>Strona główna</div>} />
          </Routes>
        </MemoryRouter>
      </AuthContext.Provider>
    )
  }

  test('shows modal and allows login when not authenticated', async () => {
    renderWithAuth({ isAuthenticated: false, user: null })

    expect(screen.getByText(/dostęp wymaga zalogowania/i)).toBeInTheDocument()
    expect(screen.getByText(/przykro nam, aby wejść/i)).toBeInTheDocument()

    fireEvent.click(screen.getByText(/przejdź do logowania/i))

    await waitFor(() => {
      expect(screen.getByText(/strona logowania/i)).toBeInTheDocument()
    })
  })

  test('redirects to home page on clicking "wróć na stronę główną"', async () => {
    renderWithAuth({ isAuthenticated: false, user: null })

    expect(screen.getByText(/dostęp wymaga zalogowania/i)).toBeInTheDocument()

    fireEvent.click(screen.getByText(/wróć na stronę główną/i))

    await waitFor(() => {
      expect(screen.getByText(/strona główna/i)).toBeInTheDocument()
    })
  })

  test('renders children when authenticated', () => {
    renderWithAuth({ isAuthenticated: true, user: { role: 'USER' } })

    expect(screen.getByText(/chroniona zawartość/i)).toBeInTheDocument()
  })

  test('blocks access if missing required role', async () => {
    renderWithAuth({ isAuthenticated: true, user: { role: 'USER' } }, 'ADMIN')

    expect(screen.queryByText(/chroniona zawartość/i)).not.toBeInTheDocument()

    await waitFor(() => {
      expect(screen.getByText(/strona główna/i)).toBeInTheDocument()
    })
  })

  test('allows access if user has required role', () => {
    renderWithAuth({ isAuthenticated: true, user: { role: 'ADMIN' } }, 'ADMIN')

    expect(screen.getByText(/chroniona zawartość/i)).toBeInTheDocument()
  })
})
