import React from 'react';
import styles from './ProjectHubPage.module.css';

import ProjectCard from '../components/ProjectCard';

export default function ProjectHubPage() {
  
  const projects = [
    { id: 'proj1', title: 'Project1' },
    { id: 'proj2', title: 'Project2' },
    { id: 'proj3', title: 'Project3' },
    { id: 'proj4', title: 'Project4' },
    { id: 'proj5', title: 'Project5' },
    { id: 'superlong', title: 'Insert Super Long Project Name So I Know To Take Care of This Case' },
    { id: 'proj7', title: 'Project7' },
  
  ];

  return (
    <div className = {styles['project-hub']}>
      <h2>Project Hub</h2>
      <div className={styles['grid']}>
        {projects.map(project => (
          <ProjectCard key={project.id} projectId={project.id} project-title={project.title} />
        ))}
      </div>
    </div>
  );
}
