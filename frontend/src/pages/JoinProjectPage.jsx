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
      const resp = await fetch(`http://localhost:8080/api/projects/getByShareCode?shareCode=${shareCodeInput}`);
      
      let data;
      try {
        data = await resp.json();
      } catch (err) {
        console.error('Failed to parse JSON from response', err);
        setErrorMessage('Invalid response from server.');
        return;
      }

      if (!resp.ok) {
        console.error('Join project error:', data);
        setErrorMessage(data.message || 'Failed to get project from share code.');
        return;
      }
     
      const projectID = data.projectID;
      if (projectID) {
        setProjects(Array.isArray(data) ? data : [data]);
        const userID = localStorage.getItem('userID');
        const postResp = await fetch(`http://localhost:8080/api/projects/joinProject/${projectID}/user/${userID}?shareCode=${shareCodeInput}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
        });

        if (postResp.ok) {
          console.log('Successfully joined the project');
        } else {
          const errorData = await postResp.json().catch(() => ({}));
          console.error('Join project error:', errorData);
          setErrorMessage(errorData.message || errorData.error || 'Failed to join the project. Please try again.');
        }


      } else {
        setErrorMessage('Project ID not found.');
        setProjects([]);
      }

  
      //   if (postResp.status === 200) {
      //     // Handle successful join (maybe navigate or show a success message)
      //     console.log('Successfully joined the project');
      //   } else {
      //     // Handle failure (display error message)
      //     setErrorMessage('Failed to join the project. Please try again.');
      //   }
      // } else {
      //   setErrorMessage('Project ID not found.');
      //   setProjects([]);
      // }
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
