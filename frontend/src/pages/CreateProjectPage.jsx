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

  const handleSubmission = async (event) => {
    event.preventDefault(); 

    if ((startDate && endDate) && (new Date(startDate) > new Date(endDate))) { // startDate && endDate checks if both fields are not empty, create a Date object and check startDate < endDate
    setError('End date cannot be before start date.'); 
    return; 
  }

  const newProject = {
    projectName,
    projectDescription,
    startDate,
    endDate,
    shareCode
  }
  console.log('Project Created:', { projectName, projectDescription, startDate, endDate });

  try {
    const userID = localStorage.getItem('userID');
    console.log('UserID: ', userID); 
    // check if this is correct
    const response = await fetch(`http://localhost:8080/api/projects/createProject?userID=${userID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newProject),
    });

    if (response.ok) {
      const result = await response.json();
      console.log('Project saved:', result);

      const projectID = result.projectID; 
      localStorage.setItem('projectID', projectID); 
      // Lead to project confirmation page 
      navigate('/'); 
    } else {
      const errorData = await response.text();
      if (response.status === 409) { // 409 since backend: HttpStatus.CONFLICT
        setError('A project with the same share code already exists.');
      } else {
        setError(errorData || 'Failed to create project. Please try again.');
        console.error('Failed to create project');
        const error = await response.json();
        console.error('Error:', error);
      }
      
    }
  } catch (error) {
    console.error('Error sending to backend:', error);
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