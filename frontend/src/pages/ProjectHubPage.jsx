import React from 'react';
import styles from './ProjectHubPage.module.css';
import ProjectCard from '../components/ProjectCard';

export default function ProjectHubPage() {
  const [projects, setProjects]           = React.useState(null);
  const [errorMessage, setErrorMessage]   = React.useState('');
  const [projectIDInput, setProjectIDInput] = React.useState('');
  const [isLoggedIn, setIsLoggedIn]       = React.useState(false);
  const [ownership, setOwnership] = React.useState({});

  const fetchUserProjects = async () => {
    try {
      const userID  = localStorage.getItem('userID');
      const resp= await fetch(`http://localhost:8080/api/users/${userID}/projects`);
      if (resp.status === 404) {
        setProjects([]); 
        setErrorMessage('You have no projects yet.');
        return;
      }
      const data    = await resp.json();
      if (!data || (Array.isArray(data) && data.length === 0)) {
        setProjects([]);
        setErrorMessage('You have no projects yet.');
      } else {
        setProjects(Array.isArray(data) ? data : [data]);
        checkOwnership(Array.isArray(data) ? data : [data]); 
      }
    } catch (e) {
      console.error(e);
      setErrorMessage('Failed to load your projects.');
      setProjects([]);
    }
  };

  React.useEffect(() => {
    const loggedIn = localStorage.getItem('logged-in') === 'true';
    setIsLoggedIn(loggedIn);
    if (loggedIn) {
      fetchUserProjects();
    }
  }, []);

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

  React.useEffect(() => {
    const loggedIn = localStorage.getItem('logged-in') === 'true';
    setIsLoggedIn(loggedIn);
    if (loggedIn) {
      fetchUserProjects();
    }
  }, []);


  const handleProjectIDSubmit = async () => {
    setErrorMessage('');
    setProjects(null); 
    if (!projectIDInput.trim()) {
      setErrorMessage('Please enter a Project ID.');
      return;
    }
    try {
      const resp = await fetch(`http://localhost:8080/api/projects/${projectIDInput}`);
      if (resp.status === 404) {
        setProjects([]); 
        setErrorMessage(`Project "${projectIDInput}" not found.`);
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
          <p>Please enter a project ID to view:</p>
          <input
            type="text"
            value={projectIDInput}
            onChange={e => setProjectIDInput(e.target.value)}
            placeholder="Enter Project ID"
            className={styles['project-id-input']}
          />
          <button
            onClick={handleProjectIDSubmit}
            className={styles['submit-button']}
          >
            Submit
          </button>

          {errorMessage && (
            <div className={styles['no-projects']}>{errorMessage}</div>
          )}

          {/* only show cards once we have results */}
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

