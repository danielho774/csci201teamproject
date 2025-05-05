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
  const [error, setError] = useState('');
  const navigate = useNavigate();

  // const {userID} = useContext(AuthContext); 

  const handleSubmission = async (event) => {
    event.preventDefault(); 

    if ((startDate && endDate) && (new Date(startDate) > new Date(endDate))) { // startDate && endDate checks if both fields are not empty, create a Date object and check startDate < endDate
    setError('End date cannot be before start date.'); 
    return; 
  }

  const code = Array.from({length: 5}, () =>
    'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'[Math.floor(Math.random() * 36)]
  ).join('');

  const newProject = {
    projectName,
    projectDescription,
    startDate,
    endDate,
    shareCode: code
  }

  console.log('Project Created:', newProject);

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
      localStorage.setItem('projectName', projectName); 
      localStorage.setItem('projectCode', code); 
      // Lead to project confirmation page 
      navigate('/confirmed'); 
    } else {
      console.error('Failed to create project');
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
          
        
        {error && <p className={styles['error']}>{error}</p>}

        <input type="submit" name="submitButton" placeholder = "Project Name"></input>

        </form>


        </div>
    </div>
  );
}


// check why the date year can go up to 5 digits 