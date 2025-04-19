import React from 'react';
import styles from './ProjectHubPage.module.css';

import ProjectCard from '../components/ProjectCard';

export default function ProjectHubPage() {
  return (
    <div className = {styles['project-hub']}>
      <h2>Project Hub</h2>
      <div className={styles['grid']}>
      <ProjectCard project-title = "Project1" />
      <ProjectCard project-title = "Project2" />
      <ProjectCard project-title = "Project3" />
      <ProjectCard project-title = "Project4" />
      <ProjectCard project-title = "Project5" />
      <ProjectCard project-title = "Insert Super Long Project Name So I Know To Take Care of This Case" />
      <ProjectCard project-title = "Project7" />
      <ProjectCard project-title = "Project8" />
      <ProjectCard project-title = "Project9" />
      <ProjectCard project-title = "Project10" />
      <ProjectCard project-title = "Project11" />
      <ProjectCard project-title = "Project12" />


    </div>
    </div>
    
  );
}
