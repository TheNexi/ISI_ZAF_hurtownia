import React from 'react'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import Register from '../pages/Register'  
import * as authService from '../services/auth'
import { MemoryRouter } from 'react-router-dom'

const mockedNavigate = jest.fn()

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedNavigate,
}))

jest.mock('../services/auth', () => ({
  register: jest.fn(),
}))

describe('Register component', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  test('renders all inputs and buttons', () => {
    render(
      <MemoryRouter>
        <Register />
      </MemoryRouter>
    )

    expect(screen.getByPlaceholderText(/login/i)).toBeInTheDocument()
    expect(screen.getByPlaceholderText(/email/i)).toBeInTheDocument()
    expect(screen.getByPlaceholderText(/hasło/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /zarejestruj się/i })).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /logowanie/i })).toBeInTheDocument()
  })

  test('calls register service and navigates on submit', async () => {
    const registerMock = authService.register as jest.Mock
    registerMock.mockResolvedValueOnce({})

    render(
      <MemoryRouter>
        <Register />
      </MemoryRouter>
    )

    fireEvent.change(screen.getByPlaceholderText(/login/i), { target: { value: 'user1' } })
    fireEvent.change(screen.getByPlaceholderText(/email/i), { target: { value: 'user1@example.com' } })
    fireEvent.change(screen.getByPlaceholderText(/hasło/i), { target: { value: 'pass123' } })

    fireEvent.click(screen.getByRole('button', { name: /zarejestruj się/i }))

    await waitFor(() => {
      expect(registerMock).toHaveBeenCalledWith({
        username: 'user1',
        email: 'user1@example.com',
        password: 'pass123',
      })
    })

    expect(mockedNavigate).toHaveBeenCalledWith('/login')
  })

  test('shows alert on register failure', async () => {
    const registerMock = authService.register as jest.Mock
    registerMock.mockRejectedValueOnce(new Error('fail'))

    window.alert = jest.fn()

    render(
      <MemoryRouter>
        <Register />
      </MemoryRouter>
    )

    fireEvent.change(screen.getByPlaceholderText(/login/i), { target: { value: 'user1' } })
    fireEvent.change(screen.getByPlaceholderText(/email/i), { target: { value: 'user1@example.com' } })
    fireEvent.change(screen.getByPlaceholderText(/hasło/i), { target: { value: 'pass123' } })

    fireEvent.click(screen.getByRole('button', { name: /zarejestruj się/i }))

    await waitFor(() => {
      expect(window.alert).toHaveBeenCalledWith('Rejestracja nie powiodła się')
    })

    expect(mockedNavigate).not.toHaveBeenCalled()
  })

  test('navigates to login when login button clicked', () => {
    render(
      <MemoryRouter>
        <Register />
      </MemoryRouter>
    )

    fireEvent.click(screen.getByRole('button', { name: /logowanie/i }))

    expect(mockedNavigate).toHaveBeenCalledWith('/login')
  })
})
