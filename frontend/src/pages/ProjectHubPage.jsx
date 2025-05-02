import React from 'react';
import styles from './ProjectHubPage.module.css';

import ProjectCard from '../components/ProjectCard';

export default function ProjectHubPage() {
  //create a function to fetch projects from the backend
  // and display them in the project hub page
  const [projects, setProjects] = React.useState([]);
  const [noProjects, setNoProjects] = React.useState(false);

  const fetchProjects = async () => {
    try {
      const userID = localStorage.getItem('userID');
      const response = await fetch(`http://localhost:8080/api/users/${userID}/projects`, {
        method: 'GET', 
        headers: {
          'Content-Type': 'application/json', 
        }, 
      }); 
    
      if (!response.ok) {
        throw new Error('Invalid Request.'); 
      }

      if (response.status === 204) {
        console.log('No projects found for this user.'); 
        setNoProjects(true);
        return; 
      }
      const projects = await response.json();
      console.log('Projects: ', projects);
      setProjects(projects);
    }
    catch(error) {
      console.error('Fetch failed: ', error.message); 
      alert('Fetch failed: ' + error.message); 
    }
  }

  // Call the fetchProjects function to fetch projects from the backend
  React.useEffect(() => {
    fetchProjects();
  }, []);


  return (
    <div className = {styles['project-hub']}>
      <h2>Project Hub</h2>
      <div className={styles['grid']}>
        {projects.map(project => (
          <ProjectCard key={project.id} projectId={project.projectID} project-title={project.projectName} />
        ))}
      </div>
      {noProjects === true &&
        <div>if true show</div>
      }
    </div>
  );
}
