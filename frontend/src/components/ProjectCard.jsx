import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import styles from './ProjectCard.module.css';
import { useNavigate } from 'react-router-dom'

function ProjectCard({projectId, 'project-title': projectTitle, onLeaveProject, isOwner}) {

    // console.log({onLeaveProject});
    
    // const { projectId } = props;
    const [isMenuOpen, setIsMenuOpen] = useState(false);
    const toggleMenu = (e) => {
        e.stopPropagation();
        e.preventDefault();
        setIsMenuOpen(prevState => !prevState);
    };

    const boardLink = `/projects/${projectId}/board`;
    const navigate = useNavigate()

    // console.log(`Check for ownership: : ${projectId}, isOwner: ${isOwner}`); 

    return (
        <Link to={boardLink} className={styles.cardLink}>
            <div className={styles['wrapper']}>
                <div className={styles['project-card']}>
                    <div className={styles['project-text']}>
                        <h1 className = {styles['project-title']}>{projectTitle}</h1>
                        <div className={styles['menu-button']} onClick={toggleMenu}>☰</div>
                    </div>
                </div>
                {isMenuOpen && (
                    <div className={styles['dropdown-menu']} onClick={(e) => { e.stopPropagation(); e.preventDefault(); }}>
                        <button onClick={() => onLeaveProject(projectId)} className={styles['dropdown-button']}>Leave Project</button>

                        {isOwner && (
                            <button onClick={() => navigate(`/projects/${projectId}/ownership`)} className={styles['dropdown-button']}>Ownership</button>
                        )}
                    </div>
                )}
            </div>
        </Link>
    )
}

export default ProjectCard; 
