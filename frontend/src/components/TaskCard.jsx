import React from 'react';
import styles from './TaskCard.module.css';


function ProjectCard(props) {
    return (
        <div className = {styles['wrapper']}>
            <div className = {styles['task-card']}></div>
            <h1 className = {styles['task-title']}>{props['task-title']}</h1>
        </div>
    )
}

export default ProjectCard; 