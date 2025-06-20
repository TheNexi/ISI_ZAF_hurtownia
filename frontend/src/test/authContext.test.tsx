import React, { useContext } from 'react'
import { render, waitFor } from '@testing-library/react'
import axios from 'axios'
import { AuthContext, AuthProvider, AuthContextType } from '../auth/AuthContext'

jest.mock('axios')
const mockedAxios = axios as jest.Mocked<typeof axios>

const TestComponent = () => {
  const auth = useContext(AuthContext)
  return (
    <>
      <div>Authenticated: {auth.isAuthenticated ? 'yes' : 'no'}</div>
      <div>User: {auth.user ? auth.user.login : 'none'}</div>
      <button onClick={auth.logout}>Logout</button>
    </>
  )
}

describe('AuthContext', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    delete (window as any).location
    ;(window as any).location = { href: '' }
  })

  test('provides default context values', () => {
    let contextValue: AuthContextType | undefined

    const Consumer = () => {
      contextValue = useContext(AuthContext)
      return null
    }

    render(<Consumer />)

    expect(contextValue).toBeDefined()
    expect(contextValue?.isAuthenticated).toBe(false)
    expect(contextValue?.user).toBeNull()
    expect(typeof contextValue?.logout).toBe('function')
  })

  test('sets user and isAuthenticated when /auth/me succeeds', async () => {
    const userData = { login: 'testuser', email: 'test@test.com', role: 'USER' }
    mockedAxios.get.mockResolvedValueOnce({ data: userData })

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    )

    await waitFor(() => {
      expect(getByText(/Authenticated: yes/i)).toBeInTheDocument()
      expect(getByText(/User: testuser/i)).toBeInTheDocument()
    })
  })

  test('sets unauthenticated and null user when /auth/me fails', async () => {
    mockedAxios.get.mockRejectedValueOnce(new Error('Unauthorized'))

    const { getByText } = render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    )

    await waitFor(() => {
      expect(getByText(/Authenticated: no/i)).toBeInTheDocument()
      expect(getByText(/User: none/i)).toBeInTheDocument()
    })
  })

    test('logout removes token, resets state and redirects', async () => {
    mockedAxios.get.mockResolvedValueOnce({ data: { login: 'testuser', email: 'test@test.com', role: 'USER' } })

    mockedAxios.post.mockResolvedValueOnce({}) 

    localStorage.setItem('token', '123')

    const { getByText } = render(
        <AuthProvider>
        <TestComponent />
        </AuthProvider>
    )

    getByText('Logout').click()

    await waitFor(() => {
        expect(localStorage.getItem('token')).toBeNull()
        expect(window.location.href).toBe('/login')
    })
  })
})
