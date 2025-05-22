import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { register as registerService } from '../../services/auth'

const Register = () => {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await registerService({ username, email, password })
      navigate('/login')
    } catch (err) {
      alert('Rejestracja nie powiodła się')
    }
  }
  const handleLogin = () => {
    navigate('/login')
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Rejestracja</h2>
      <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Login" />
      <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Hasło" />
      <button type="submit">Zarejestruj się</button>

      <div className="form-extra">
        <hr className="divider" />
        <p className="form-info">Masz już konto? Zaloguj się!</p>
        <button type="button" onClick={handleLogin}>Logowanie</button>
      </div>
    </form>
  )
}

export default Register
