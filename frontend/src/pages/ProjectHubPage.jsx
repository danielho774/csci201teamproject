import React from 'react';
import styles from './ProjectHubPage.module.css';

import ProjectCard from '../components/ProjectCard';

export default function ProjectHubPage() {
  //create a function to fetch projects from the backend
  // and display them in the project hub page
  const [projects, setProjects] = React.useState([]);
  const [noProjects, setNoProjects] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState('');

  const fetchProjects = async () => {
    try {
      const userID = localStorage.getItem('userID');
      const response = await fetch(`http://localhost:8080/api/users/${userID}/projects`, {
        method: 'GET', 
        headers: {
          'Content-Type': 'application/json', 
        }, 
      }); 
      const jsonResponse = await response.json();

      if (response.status === 404) {
        console.log(jsonResponse); 
        setNoProjects(true);
        setErrorMessage(jsonResponse.message)
        return; 
      }

      console.log('Projects: ', jsonResponse);
      setProjects(jsonResponse);
    }
    catch(error) {
      console.error('Fetch failed: ', error.message); 
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
      {noProjects && <div className={styles['no-projects']}>{errorMessage}</div>}
    </div>
  );
}
