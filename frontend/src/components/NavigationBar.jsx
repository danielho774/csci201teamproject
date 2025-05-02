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
    // console.log("user logged out")
    setIsLoggedIn(false); 
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
              <Link to="/tasks" className={styles.navLink}>Individual Tasks</Link>
           </li>
          <li className={styles.navItem}>
            <Link to="/create" className={styles.navLink}>Create Project</Link>
          </li>
          <li className={styles.navItem}>
            <Link to="/availability" className={styles.navLink}>Availability</Link>
          </li>
          <li className={styles.navItem}>
            <button onClick={handleLogout} className = {`${styles.navLink} ${styles.logoutButton}`}>Logout</button>
          </li>
        </>
        ) : (
          <li className = {styles.navItem}>
            <Link to = "/login" className = {styles.navLink}>Login</Link>
          </li>
        )}
      </ul>
    </nav>
  ); 
}

export default NavigationBar; 