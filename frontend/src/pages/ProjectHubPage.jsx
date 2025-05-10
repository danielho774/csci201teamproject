import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import styles from './ProjectHubPage.module.css';
import ProjectCard from '../components/ProjectCard';

export default function ProjectHubPage() {
  const [projects, setProjects]           = React.useState(null);
  const [errorMessage, setErrorMessage]   = React.useState('');
  const [shareCodeInput, setShareCodeInput] = React.useState('');
  const [isLoggedIn, setIsLoggedIn]       = React.useState(false);
  const [ownership, setOwnership] = React.useState({});
  const location = useLocation();

  const fetchUserProjects = async () => {
    try {
      const userID = localStorage.getItem('userID');
      console.log('Fetching projects for user:', userID);
      
      const resp = await fetch(`http://localhost:8080/api/users/${userID}/projects`);
      console.log('Response status:', resp.status);
      
      const data = await resp.json();
      console.log('Fetched projects:', data);

      if (resp.status === 404) {
        setProjects([]); 
        setErrorMessage('You have no projects yet.');
        return;
      }

      if (!data || (Array.isArray(data) && data.length === 0)) {
        setProjects([]);
        setErrorMessage('You have no projects yet.');
      } else if (data.message) {
        // Handle error message from backend
        setProjects([]);
        setErrorMessage(data.message);
      } else {
        setProjects(Array.isArray(data) ? data : [data]);
        checkOwnership(Array.isArray(data) ? data : [data]);
      }
    } catch (e) {
      console.error('Error fetching projects:', e);
      setErrorMessage('Failed to load your projects.');
      setProjects([]);
    }
  };

  useEffect(() => {
    const loggedIn = localStorage.getItem('logged-in') === 'true';
    setIsLoggedIn(loggedIn);
    if (loggedIn) {
      fetchUserProjects();
    }
  }, [location.state?.refresh]);

  const checkOwnership = async (projects) => {
    const userID = localStorage.getItem('userID');
    const ownershipStatus = {};
    for (const project of projects) {
      try {
        const resp = await fetch(`http://localhost:8080/api/members/${userID}/getOwnership/${project.projectID}`);
        const data = await resp.json();
        ownershipStatus[project.projectID] = data.role; // { true: owner, false: member }
      } catch (e) {
        console.error(e);
        ownershipStatus[project.projectID] = false; // Default to member if check fails
      }
    }
    setOwnership(ownershipStatus);
  };

  const handleShareCodeSubmit = async () => {
    setErrorMessage('');
    setProjects(null); 
    if (!shareCodeInput.trim()) {
      setErrorMessage('Please enter a Project ID.');
      return;
    }
    try {
      const resp = await fetch(`http://localhost:8080/api/projects/getByShareCode?shareCode=${shareCodeInput}`);
      if (resp.status === 404) {
        setProjects([]); 
        setErrorMessage(`Project with share code "${shareCodeInput}" not found.`);
        return;
      }
      const data = await resp.json();
      const processedData = Array.isArray(data) ? data : [data]; 
      setProjects(processedData);
      checkOwnership(processedData); 

    } catch (e) {
      console.error(e);
      setErrorMessage('Lookup failed. Try again.');
      setProjects([]);
    }
  };

  const handleLeaveProject = async (projectId) => {
    const userID = localStorage.getItem('userID');
    try {
      const resp = await fetch(`http://localhost:8080/api/members/${userID}/leave/${projectId}`, {
        method: 'DELETE',
      });

      if (resp.ok) {
        setProjects(projects.filter(project => project.projectID !== projectId));
        setErrorMessage('Successfully left the project.');
      } else {
        const errorData = await resp.json();
        setErrorMessage(errorData.message || 'Failed to leave the project.');
      }
    } catch (e) {
      console.error(e);
      setErrorMessage('Failed to leave the project. Try again.');
    }
  };

  return (
    <div className={styles['project-hub']}>
      <h2>Project Hub</h2>

      {isLoggedIn ? (
        <>
          {projects === null ? (
            <p>Loading your projects…</p>
          ) : projects.length > 0 ? (
            <div className={styles.grid}>
              {projects.map(p => {
                const isOwner = ownership[p.projectID]; 
                const currentUserId = localStorage.getItem('userID'); 
                return (
                  <ProjectCard
                  key={p.projectID}
                  projectId={p.projectID}
                  project-title={p.projectName}
                  onLeaveProject={handleLeaveProject}
                  isOwner={ownership[p.projectID]}
                  currentUserId = {currentUserId}
                />
                ); 
    })}
            </div>
          ) : (
            <p className={styles['no-projects']}>{errorMessage}</p>
          )}
        </>
      ) : (
       
        <div className={styles['input-wrapper']}>
          <p>Please enter a project share code to view:</p>
          <input
            type="text"
            value={shareCodeInput}
            onChange={e => setShareCodeInput(e.target.value)}
            placeholder="Enter share code"
          />
          <button
            onClick={handleShareCodeSubmit}
            className={styles['submit-button']}
          >
            Submit
          </button>

          {errorMessage && (
            <div className={styles['no-projects']}>{errorMessage}</div>
          )}

          {projects && projects.length > 0 && (
            <div className={styles.grid}>
              {projects.map(p => (
                <ProjectCard
                  key={p.projectID}
                  projectId={p.projectID}
                  project-title={p.projectName}
                  isOwner={ownership[p.projectID]}
                />
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

