import React, { useContext, useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { AuthContext } from '../../auth/AuthContext'
import axios from 'axios'

const Login = () => {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const { setAuthenticated, isAuthenticated } = useContext(AuthContext)
  const navigate = useNavigate()

  useEffect(() => {
    const token = localStorage.getItem('token')
    if (token) {
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      setAuthenticated(true)  
    }
  }, [setAuthenticated])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const response = await axios.post(
        'http://localhost:8080/api/auth/login',
        { username, password },
        { withCredentials: true }
      )

      const token = response.data.token
      if (token) {
        localStorage.setItem('token', token)
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
        setAuthenticated(true)
        navigate('/')
      } else {
        console.warn('Brak tokena w odpowiedzi')
      }
    } catch {
      alert('Nieprawidłowy login lub hasło')
    }
  }

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/')
    }
  }, [isAuthenticated, navigate])

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google'
  }

  const handleRegister = () => {
    navigate('/register')
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Logowanie</h2>
      <input
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        placeholder="Login"
        required
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Hasło"
        required
      />
      <button type="submit">Zaloguj się</button>
      <button type="button" onClick={handleGoogleLogin}>
        Zaloguj się przez Google
      </button>

      <hr className="divider" />

      <div className="form-extra">
        <p className="form-info">Nie posiadasz konta? Stwórz je!</p>
        <button type="button" onClick={handleRegister}>
          Rejestracja
        </button>
      </div>
    </form>
  )
}

export default Login
