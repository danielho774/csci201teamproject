import React from 'react';
import styles from './ProjectHubPage.module.css';

import ProjectCard from '../components/ProjectCard';

export default function ProjectHubPage() {
  //create a function to fetch projects from the backend
  // and display them in the project hub page
  const [projects, setProjects] = React.useState([]);
  const [noProjects, setNoProjects] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState('');
  const [projectIDInput, setProjectIDInput] = React.useState('');
  const [isLoggedIn, setIsLoggedIn] = React.useState(false);


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
      setProjects([jsonResponse]);
      setNoProjects(false);
    }
    catch(error) {
      console.error('Fetch failed: ', error.message); 
    }
  }

  // Call the fetchProjects function to fetch projects from the backend
  React.useEffect(() => {
    const checkLoginAndFetchProjects = async () => {
      const isLoggedIn = localStorage.getItem("logged-in");
      setIsLoggedIn(localStorage.getItem("logged-in") === "true");
      console.log("isLoggedin: " + isLoggedIn);
      if (isLoggedIn) {
        await fetchProjects();
      }
    };
  
    checkLoginAndFetchProjects();
  }, []);

  const handleProjectIDSubmit = async () => {
    console.log('Submitted Project ID:', projectIDInput);
    // Add logic here to fetch public project info or redirect
    try {
      const response = await fetch(`http://localhost:8080/api/projects/${projectIDInput}`, {
        method: 'GET', 
        headers: {
          'Content-Type': 'application/json', 
        }, 
      }); 
      const jsonResponse = await response.json();

      if (response.status === 404) {
        setProjects([]);
        console.log(jsonResponse); 
        setNoProjects(true);
        // setErrorMessage(jsonResponse.message)
        setErrorMessage(`Project with ID ${projectIDInput} not found.`);
        return; 
      }

      console.log('Projects: ', jsonResponse);
      if (Array.isArray(jsonResponse)) {
        setProjects(jsonResponse);
      } else {
        setProjects([jsonResponse]);
      }
    }
    catch(error) {
      console.error('Fetch failed: ', error.message); 
    }
  };

  return (
    <div className={styles['project-hub']}>
      <h2>Project Hub</h2>
      {isLoggedIn ? (
        <>
          <div className={styles['grid']}>
            {projects.map(project => (
              <ProjectCard
                key={project.id || project.projectID}
                projectId={project.projectID}
                project-title={project.projectName}
              />
            ))}
          </div>
          {noProjects && <div className={styles['no-projects']}>{errorMessage}</div>}
        </>
      ) : (
        
        <div className={styles['input-wrapper']}>
          <p>Please enter a project ID to view:</p>
          <input
            type="text"
            value={projectIDInput}
            onChange={(e) => setProjectIDInput(e.target.value)}
            placeholder="Enter Project ID"
            className={styles['project-id-input']}
          />
          <button onClick={handleProjectIDSubmit} className={styles['submit-button']}>
            Submit
          </button>
          {noProjects && <div className={styles['no-projects']}>{errorMessage}</div>}

          {projects.length > 0 && !noProjects && (
            <div className={styles['grid']}>
              {projects.map((project) => (
                <ProjectCard
                  key={project.id || project.projectID}
                  projectId={project.projectID}
                  project-title={project.projectName}
                />
              ))}
            </div>
          )}

        </div>
      )}
    </div>
  );
}