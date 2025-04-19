import React from 'react';
import styles from './ProjectCard.module.css';


function ProjectCard(props) {
    return (
        <div className = {styles['wrapper']}>
            <div className = {styles['project-card']}></div>
                <div className={styles['project-text']}>
                    <h1 className = {styles['project-title']}>{props['project-title']}</h1>
                    <div className = {styles['menu-button']}>☰</div>
                </div>
        </div>
    )
}

export default ProjectCard; 