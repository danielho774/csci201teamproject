import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import styles from './ProjectCard.module.css';

function ProjectCard(props) {
    const { projectId } = props;
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const toggleMenu = (e) => {
        e.stopPropagation();
        e.preventDefault();
        setIsMenuOpen(prevState => !prevState);
    };

    const boardLink = `/projects/${projectId}/board`;

    return (
        <Link to={boardLink} className={styles.cardLink}>
            <div className={styles['wrapper']}>
                <div className={styles['project-card']}>
                    <div className={styles['project-text']}>
                        <h1 className = {styles['project-title']}>{props['project-title']}</h1>
                        <div className={styles['menu-button']} onClick={toggleMenu}>☰</div>
                    </div>
                </div>
                {isMenuOpen && (
                    <div className={styles['dropdown-menu']} onClick={(e) => { e.stopPropagation(); e.preventDefault(); }}>
                        <button className={styles['dropdown-button']}>Leave Project</button>
                        <button className={styles['dropdown-button']}>Transfer Ownership</button>
                    </div>
                )}
            </div>
        </Link>
    )
}

export default ProjectCard; 