import React, { useContext, useState, useEffect } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { AuthContext } from '../../auth/AuthContext'
import axios from 'axios'

const Login = () => {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const { setAuthenticated, isAuthenticated, setUser } = useContext(AuthContext)
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await axios.post(
        'http://localhost:8080/api/auth/login',
        { username, password },
        { withCredentials: true }
      )

      const userRes = await axios.get('http://localhost:8080/auth/me', {
        withCredentials: true,
      })

      const user = userRes.data
      if (!user || !user.role) {
        throw new Error('Brak danych użytkownika')
      }

      setUser(user)
      setAuthenticated(true)

      if (user.role === 'ADMIN') {
        navigate('/admin')
      } else {
        navigate('/')
      }
    } catch (err: any) {
      if (err.response?.data) {
        alert(err.response.data)
      } else {
        alert('Nieprawidłowy login lub hasło')
      }
    }
  }

  useEffect(() => {
    if (isAuthenticated) {
      axios
        .get('http://localhost:8080/auth/me', { withCredentials: true })
        .then(res => {
          const user = res.data
          if (user.role === 'ADMIN') {
            navigate('/admin')
          } else {
            navigate('/')
          }
        })
        .catch(() => {})
    }
  }, [isAuthenticated, navigate])

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google'
  }

  const handleRegister = () => {
    navigate('/register')
  }

  return (
    <>
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

      <div className="browse-guest">
        <Link to="/" className="btn btn-primary btn-browse">
          Przeglądaj bez logowania <span className="arrow">→</span>
        </Link>
      </div>
    </>
  )
}

export default Login
