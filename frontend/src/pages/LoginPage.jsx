import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styles from './LoginPage.module.css';

// Receive onLogin prop from App.jsx
export default function LoginPage({ onLogin }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    
    console.log('Attempting login with:', username, password);
    // Assuming login is successful NEEDS TO BE CHANGED
    try {
      const response = await fetch('http://localhost:8080/api/login', {
        method: 'POST', 
        headers: {
          'Content-Type': 'application/json', 
        }, 
        body: JSON.stringify({username, password}),
      }); 

      if (!response.ok) {
        throw new Error('Invalid login.'); 
      }

      const userData = await response.json(); 
      console.log('Logged in user: ', userData); 

      onLogin(userData); 
      navigate('/'); 
    }
    catch(error) {
      console.error('Login failed: ', error.message); 
      alert('Login failed: ' + error.message); 
    } 
  };

  return (
    <div className={styles.loginPage || 'login-page'}>
      <h2>Login</h2>
      <form onSubmit={handleFormSubmit}>
        <div>
          <label htmlFor="username">Username:</label>
          <input 
            type="text" 
            id="username" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
            required 
          />
        </div>
        <div>
          <label htmlFor="password">Password:</label>
          <input 
            type="password" 
            id="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            required 
          />
        </div>
        <button type="submit">Login</button>
      </form>
      <p>
        Don't have an account? <Link to="/register">Register here</Link>
      </p>
    </div>
  );
}
