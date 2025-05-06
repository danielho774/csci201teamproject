import React from 'react';
import { useNavigate } from 'react-router-dom'; 
import styles from './JoinProjectPage.module.css';
import ProjectCard from '../components/ProjectCard';

export default function JoinProjectPage() {
  const [projects, setProjects]           = React.useState(null);
  const [errorMessage, setErrorMessage]   = React.useState('');
  const [shareCodeInput, setShareCodeInput] = React.useState('');
  const navigate = useNavigate();

  const handleShareCodeSubmit = async () => {
    setErrorMessage('');
    setProjects(null); 
    if (!shareCodeInput.trim()) {
      setErrorMessage('Please enter share code.');
      return;
    }
    try {
      // waiting for try fetching without needing projectID
      const resp = await fetch(`http://localhost:8080/api/projects/joinProject/${shareCodeInput}`);
      if (resp.status === 404) {
        setProjects([]); 
        setErrorMessage(`Project with share code "${shareCodeInput}" not found.`);
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
    <div className={styles['join-project']}>
      <h2>Join Project</h2>       
        <div className={styles['input-wrapper']}>
          <p>Please enter share code to join:</p>
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

          {/* only show cards once we have results */}
          {projects && projects.length > 0 && (
            <div className={styles.grid}>
              {/* {projects.map(p => (
                <ProjectCard
                  key={p.projectID}
                  projectId={p.projectID}
                  project-title={p.projectName}
                />
              ))} */}
            </div>
          )}
        </div>
    </div>
  );
}
