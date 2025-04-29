import React from 'react';
import { Link } from 'react-router-dom'; 
import styles from './NavigationBar.module.css'; 

// Receive isLoggedIn and onLogout as props
function NavigationBar({ isLoggedIn, onLogout }) {
  return (
    <nav className={styles.navbar}>
      <ul className={styles.navList}>
        <li className={styles.navItem}>
         
          <Link to="/" className={styles.navLink}>Project Hub</Link>
        </li>
        <li className={styles.navItem}>
          <Link to="/tasks" className={styles.navLink}>Individual Tasks</Link>
        </li>
        <li className={styles.navItem}>
          <Link to="/create" className={styles.navLink}>Create Project</Link>
        </li>
        <li className={styles.navItem}>
          <Link to="/availability" className={styles.navLink}>Availability</Link>
        </li>
        {/* Conditional Login/Logout */}
        <li className={styles.navItem}>
          {isLoggedIn ? (
            <button onClick={onLogout} className={`${styles.navLink} ${styles.logoutButton}`}>Logout</button>
          ) : (
            <Link to="/login" className={styles.navLink}>Login</Link>
          )}
        </li>
       
      </ul>
    </nav>
  );
}

export default NavigationBar; 