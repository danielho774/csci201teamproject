import React, {useEffect, useState} from 'react';
import { Link } from 'react-router-dom'; 
import styles from './NavigationBar.module.css'; 

// Receive isLoggedIn and onLogout as props
function NavigationBar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false); 

  useEffect(() => {
    const userID = localStorage.getItem('userID'); 
    if (userID) {
      setIsLoggedIn(true); 
    }
    else {
      setIsLoggedIn(false); 
    }
  }); 

  const handleLogout = () => {
    localStorage.removeItem('userID'); 
    localStorage.setItem('logged-in', 'false');
    // console.log("user logged out")
    setIsLoggedIn(false); 

    window.location.href = '/login';
  }; 

  return (
    <nav className={styles.navbar}>
      <ul className={styles.navList}>
        <li className={styles.navItem}>
         
          <Link to="/" className={styles.navLink}>Project Hub</Link>
        </li>

        {isLoggedIn ? (
          <>
          <li className={styles.navItem}>
            <Link to="/create" className={styles.navLink}>Create Project</Link>
          </li>
          <li className={styles.navItem}>
            <Link to="/join" className={styles.navLink}>Join Project</Link>
          </li>
          <li className={styles.navItem}>
             <Link to="/availability" className={styles.navLink}>Availability</Link>
           </li>
          <li className={styles.navItem}>
            <Link to="/logout" className={styles.navLink}>Logout</Link>
          </li>
        </>
        ) : (
          <li className = {styles.navItem}>
            <Link to = "/login" className = {styles.navLink}>Login/Sign up</Link>
          </li>
        )}
      </ul>
    </nav>
  ); 
}

export default NavigationBar; 