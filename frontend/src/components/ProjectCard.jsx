import React, {useState} from 'react';
import styles from './ProjectCard.module.css';


function ProjectCard(props) {
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const toggleMenu = () => {
        setIsMenuOpen(prevState => !prevState); // Toggle the state
    };

    return (

        <div className = {styles['wrapper']}>
            <div className = {styles['project-card']}></div>
                <div className={styles['project-text']}>
                    <h1 className = {styles['project-title']}>{props['project-title']}</h1>
                    <div className = {styles['menu-button']} onClick={toggleMenu}>☰</div>
                </div>
        {isMenuOpen && (
            <div className={styles['dropdown-menu']}>
                <button className={styles['dropdown-button']}>Leave Project</button>
                <button className={styles['dropdown-button']}>Transfer Ownership</button>
            </div>
        )}

        </div>
    )
}

export default ProjectCard; 