import React from 'react';
import styles from './CreateProjectPage.module.css';

export default function CreateProjectPage() {
  return (
    <div className={styles['project-creation']}>
      <h2>Create New Project</h2>
      <div className={styles['project-text']}>
        <form>
          <h3>Project Name</h3>
          <input type="text" name="projectName" placeholder = "Insert project name"></input>
        </form>
        <form>
          <h3>Start Date</h3>
          <input type="date" name="startDate" placeholder = "Insert project name"></input>
        </form>
        <form>
          <h3>End Date</h3>
          <input type="date" name="endDate" placeholder = "Insert project name"></input>
        </form>
        <form>
          <h3>Project Name</h3>
          <input type="text" name="projectName" placeholder = "Insert project name"></input>
        </form>
        <input type="submit" name="submitButton" placeholder = "Project Name"></input>
        </div>
    </div>
  );
}


// make sure end date cannot be before start date