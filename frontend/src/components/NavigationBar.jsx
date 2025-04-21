import React from 'react';
import { Link } from 'react-router-dom'; // Import Link
import styles from './NavigationBar.module.css'; // We will create this file next

function NavigationBar() {
  return (
    <nav className={styles.navbar}>
      <ul className={styles.navList}>
        <li className={styles.navItem}>
          {/* Use Link component for navigation */}
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
        {/* Add more links as needed */}
      </ul>
    </nav>
  );
}

export default NavigationBar; 