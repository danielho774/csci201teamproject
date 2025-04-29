import React from 'react';
import { useParams, Link } from 'react-router-dom';
import styles from './TaskBoardPage.module.css';

export default function TaskBoardPage() {

  const { projectId } = useParams();

  
  const addTaskLink = `/projects/${projectId}/tasks/add`;

  return (
    <div className={styles.taskBoard || 'task-board'}>
      <h2>Task Board for Project: {projectId}</h2>
      
      <Link to={addTaskLink}>
        <button>Add New Task</button>
      </Link>

      
      <p>Tasks for this project will be displayed here...</p>
     

    </div>
  );
}
