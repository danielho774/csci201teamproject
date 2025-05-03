import React, { useState } from 'react'
import './LoginSignup.css'
import { useNavigate } from 'react-router-dom'

import userIcon from '../Assets/person.png'
import emailIcon from '../Assets/email.png'
import passwordIcon from '../Assets/password.png'

export const LoginSignup = ({ onLogin }) => {
  const [username,  setUsername]  = useState("")
  const [firstName, setFirstName] = useState("")
  const [lastName,  setLastName]  = useState("")
  const [email,     setEmail]     = useState("")
  const [password,  setPassword]  = useState("")

  const [action, setAction] = useState("Log In")
  const [errors, setErrors] = useState({})         // { email: "...", password: "...", form: "..." }
  const navigate = useNavigate()

  // Perform login against backend
  async function processLogin({ username, password }) {
    try {
      const res = await fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      })
      if (!res.ok) {
        const txt = await res.text()
        throw new Error(txt || 'Error logging in')
      }
      const data = await res.json()
      localStorage.setItem('logged-in', 'true')
      localStorage.setItem('userID', data.userId)
      onLogin()
      navigate('/')
    } catch (e) {
      // show a form-level error
      setErrors({ form: e.message })
    }
  }

  // Perform signup against backend
  async function processSignup({ username, firstName, lastName, email, password }) {
    const res = await fetch('http://localhost:8080/api/users/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password, firstName, lastName }),
    })
    if (!res.ok) {
      const txt = await res.text().catch(() => '')
      // detect duplicate email (HTTP 400 + MySQL duplicate key error)
      if (res.status === 400 && txt.includes('Duplicate entry')) {
        setErrors({ email: 'The email is already registered.' })
      } else {
        setErrors({ form: txt || 'Error signing up' })
      }
      return
    }
    const data = await res.json()
    console.log("Response data from backend:", data)
    localStorage.setItem('logged-in', 'true')

    // register uses userID, whereas login uses userId
    localStorage.setItem('userID', data.userID)
    
    onLogin()
    navigate('/')
  }

  // Called when the user clicks “Log In” / “Sign Up”
  function handleSubmit(e) {
    e.preventDefault()
    setErrors({})

    if (action === 'Log In') {
      // client-side required check
      if (!username.trim() || !password) {
        const errs = {}
        if (!username.trim()) errs.username = 'Username is required.'
        if (!password) errs.password = 'Password is required.'
        return setErrors(errs)
      }
      return processLogin({ username, password })
    }

    // Sign Up validations
    const errs = {}
    if (!email.trim())     errs.email     = 'Email is required.'
    if (!username.trim())  errs.username  = 'Username is required.'
    if (!firstName.trim()) errs.firstName = 'First name is required.'
    if (!lastName.trim())  errs.lastName  = 'Last name is required.'
    if (!password)         errs.password  = 'Password is required.'

    if (Object.keys(errs).length) {
      return setErrors(errs)
    }
    processSignup({ username, firstName, lastName, email, password })
  }

  return (
    <div className="container">
      <div className="header">
        <div className="text">{action}</div>
        <div className="underline" />
      </div>

      <form className="inputs" onSubmit={handleSubmit}>
        {action === 'Sign Up' && (
          <>
            <div className="input">
              <img src={emailIcon} alt="Email" />
              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={e => setEmail(e.target.value)}
              />
              {errors.email && <div className="error">{errors.email}</div>}
            </div>
            <div className="input">
              <img src={userIcon} alt="First Name" />
              <input
                type="text"
                placeholder="First Name"
                value={firstName}
                onChange={e => setFirstName(e.target.value)}
              />
              {errors.firstName && <div className="error">{errors.firstName}</div>}
            </div>
            <div className="input">
              <img src={userIcon} alt="Last Name" />
              <input
                type="text"
                placeholder="Last Name"
                value={lastName}
                onChange={e => setLastName(e.target.value)}
              />
              {errors.lastName && <div className="error">{errors.lastName}</div>}
            </div>
          </>
        )}

        <div className="input">
          <img src={userIcon} alt="Username" />
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={e => setUsername(e.target.value)}
          />
          {errors.username && <div className="error">{errors.username}</div>}
        </div>

        <div className="input">
          <img src={passwordIcon} alt="Password" />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
          {errors.password && <div className="error">{errors.password}</div>}
        </div>

        {errors.form && <div className="error form-error">{errors.form}</div>}

        <div className="submit-container">
          <div
            className={action === 'Sign Up' ? 'submit gray' : 'submit'}
            onClick={() => setAction('Sign Up')}
          >
            Sign Up
          </div>
          <div
            className={action === 'Log In' ? 'submit gray' : 'submit'}
            onClick={() => setAction('Log In')}
          >
            Log In
          </div>
        </div>

        <button type="submit" className="submit-button">
          {action}
        </button>
      </form>
    </div>
  )
}

