import React, { useContext, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AuthContext } from '../../auth/AuthContext'
import { login as loginService } from '../../services/auth'

const Login = () => {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const { login } = useContext(AuthContext)
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const token = await loginService({ username, password })
      login(token)
      navigate('/')
    } catch (error) {
      alert('Nieprawidłowy login lub hasło')
    }
  }
  const handleRegister = () => {
    navigate('/register') 
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Logowanie</h2>
      <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Login" />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Hasło" />
      <button type="submit">Zaloguj</button>

      <button type="button" onClick={handleRegister}>Zarejestruj się</button>
    </form>
  )
}

export default Login
