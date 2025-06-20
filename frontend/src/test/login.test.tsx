const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => {
  const originalModule = jest.requireActual('react-router-dom');
  return {
    ...originalModule,
    useNavigate: () => mockedNavigate,
  };
});

import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Login from '../pages/Login';
import { AuthContext } from '../auth/AuthContext';
import { MemoryRouter } from 'react-router-dom';
import axios from 'axios';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('Login component', () => {
  const setAuthenticated = jest.fn();
  const setUser = jest.fn();
  const logout = jest.fn();

  beforeEach(() => {
    setAuthenticated.mockClear();
    setUser.mockClear();
    logout.mockClear();
    mockedNavigate.mockClear();
  });

  test('renders form and allows input', () => {
    render(
      <AuthContext.Provider
        value={{
          isAuthenticated: false,
          setAuthenticated,
          user: null,
          setUser,
          logout,
        }}
      >
        <MemoryRouter>
          <Login />
        </MemoryRouter>
      </AuthContext.Provider>
    );

    const usernameInput = screen.getByPlaceholderText(/login/i);
    const passwordInput = screen.getByPlaceholderText(/hasło/i);

    const submitButton = screen.getByRole('button', { name: /^zaloguj się$/i });

    expect(usernameInput).toBeInTheDocument();
    expect(passwordInput).toBeInTheDocument();
    expect(submitButton).toBeInTheDocument();

    fireEvent.change(usernameInput, { target: { value: 'user1' } });
    fireEvent.change(passwordInput, { target: { value: 'password1' } });

    expect(usernameInput).toHaveValue('user1');
    expect(passwordInput).toHaveValue('password1');
  });

  test('successful login flow', async () => {
    mockedAxios.post.mockResolvedValueOnce({ status: 200 });
    mockedAxios.get.mockResolvedValueOnce({
      data: { login: 'user1', email: 'user1@example.com', role: 'USER' },
    });

    render(
      <AuthContext.Provider
        value={{
          isAuthenticated: false,
          setAuthenticated,
          user: null,
          setUser,
          logout,
        }}
      >
        <MemoryRouter>
          <Login />
        </MemoryRouter>
      </AuthContext.Provider>
    );

    fireEvent.change(screen.getByPlaceholderText(/login/i), {
      target: { value: 'user1' },
    });
    fireEvent.change(screen.getByPlaceholderText(/hasło/i), {
      target: { value: 'password1' },
    });

    fireEvent.click(screen.getByRole('button', { name: /^zaloguj się$/i }));

    await waitFor(() => {
      expect(mockedAxios.post).toHaveBeenCalledWith(
        'http://localhost:8080/api/auth/login',
        { username: 'user1', password: 'password1' },
        { withCredentials: true }
      );
      expect(mockedAxios.get).toHaveBeenCalledWith('http://localhost:8080/auth/me', {
        withCredentials: true,
      });
      expect(setUser).toHaveBeenCalledWith({
        login: 'user1',
        email: 'user1@example.com',
        role: 'USER',
      });
      expect(setAuthenticated).toHaveBeenCalledWith(true);
      expect(mockedNavigate).toHaveBeenCalledWith('/');
    });
  });

  test('login error shows alert', async () => {
    window.alert = jest.fn();

    mockedAxios.post.mockRejectedValueOnce({
      response: { data: 'Błąd logowania' },
    });

    render(
      <AuthContext.Provider
        value={{
          isAuthenticated: false,
          setAuthenticated,
          user: null,
          setUser,
          logout,
        }}
      >
        <MemoryRouter>
          <Login />
        </MemoryRouter>
      </AuthContext.Provider>
    );

    fireEvent.change(screen.getByPlaceholderText(/login/i), {
      target: { value: 'wrong' },
    });
    fireEvent.change(screen.getByPlaceholderText(/hasło/i), {
      target: { value: 'wrong' },
    });

    fireEvent.click(screen.getByRole('button', { name: /^zaloguj się$/i }));

    await waitFor(() => {
      expect(window.alert).toHaveBeenCalledWith('Błąd logowania');
    });
  });
});
