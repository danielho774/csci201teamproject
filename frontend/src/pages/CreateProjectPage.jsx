import React, {useState, useContext} from 'react';
import styles from './CreateProjectPage.module.css';
import {AuthContext} from '../context/AuthContext'; 
import { useNavigate } from 'react-router-dom';

export default function CreateProjectPage() {
  /* Initialization */
  const [projectName, setProjectName] = useState('');
  const [projectDescription, setProjectDescription] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [shareCode, setShareCode] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  // const {userID} = useContext(AuthContext); 

  const handleSubmission = async (e) => {
    e.preventDefault();
    
    // Validate dates
    if (new Date(endDate) <= new Date(startDate)) {
      setError('End date must be after start date');
      return;
    }

    const userID = localStorage.getItem('userID');
    
    try {
      const response = await fetch('http://localhost:8080/api/projects/createProject?userID=' + userID, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          projectName,
          projectDescription,
          startDate: startDate.split('T')[0],
          endDate: endDate.split('T')[0],
          shareCode
        })
      });

      if (!response.ok) {
        const errorData = await response.text();
        setError(errorData || 'Failed to create project');
        return;
      }

      const project = await response.json();
      console.log('Project Created:', project); // Add this for debugging

      // After successful creation, navigate to hub
      navigate('/', { state: { refresh: true } });
    } catch (err) {
      console.error('Error creating project:', err);
      setError('Failed to create project. Please try again.');
    }
  };

  return (
    <div className={styles['project-creation']}>
      <h2>Create New Project</h2>
      <div className={styles['project-text']}>
        <form onSubmit={handleSubmission} className = {styles['project-text']}>
          <h3>Project Name</h3>
          <input type="text" name="projectName" placeholder = "Insert project name" value={projectName}
          onChange={(e) => setProjectName(e.target.value)} required></input>

          <h3>Project Description</h3>
          <input type="text" name="projectDescription" placeholder = "Insert project description" value={projectDescription}
          onChange={(e) => setProjectDescription(e.target.value)} required></input>
        
          <h3>Start Date</h3>
          <input type="date" name="startDate" value={startDate}
          onChange={(e) => {
            setStartDate(e.target.value); 
            if (endDate && new Date(e.target.value) <= new Date(endDate)) {
              setError('');
            }
          }}
          required></input>
        
          <h3>End Date</h3>
          <input type="date" name="endDate" value={endDate}
          onChange={(e) => {
            setEndDate(e.target.value); 
            if (endDate && new Date(e.target.value) <= new Date(endDate)) {
              setError('');
            }
          }}
          required></input>  

          <h3>Share Code</h3>
          <input type="text" name="shareCode" placeholder = "Choose share code" value={shareCode}
          onChange={(e) => setShareCode(e.target.value)} required></input>     
          
        
        {error && <p className={styles['error']}>{error}</p>}

        <input type="submit" name="submitButton" placeholder = "Project Name"></input>

        </form>


        </div>
    </div>
  );
}


// check why the date year can go up to 5 digits 