import React from 'react';
import styles from './ProjectHubPage.module.css';
import ProjectCard from '../components/ProjectCard';

export default function ProjectHubPage() {
  const [projects, setProjects]           = React.useState(null);
  const [errorMessage, setErrorMessage]   = React.useState('');
  const [projectIDInput, setProjectIDInput] = React.useState('');
  const [isLoggedIn, setIsLoggedIn]       = React.useState(false);

  const fetchUserProjects = async () => {
    try {
      const userID  = localStorage.getItem('userID');
      const resp    = await fetch(`http://localhost:8080/api/users/${userID}/projects`);
      if (resp.status === 404) {
        setProjects([]); 
        setErrorMessage('You have no projects yet.');
        return;
      }
      const data    = await resp.json();
      setProjects(Array.isArray(data) ? data : [data]);
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
      setProjects(Array.isArray(data) ? data : [data]);
    } catch (e) {
      console.error(e);
      setErrorMessage('Lookup failed. Try again.');
      setProjects([]);
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
              {projects.map(p => (
                <ProjectCard
                  key={p.projectID}
                  projectId={p.projectID}
                  project-title={p.projectName}
                />
              ))}
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
                />
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}
