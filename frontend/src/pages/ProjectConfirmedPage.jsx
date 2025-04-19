import React from 'react';
import styles from './CreateProjectPage.module.css';

export default function CreateProjectPage() {
  return (
    <div className={styles['project-creation']}>
      <h2>Create New Project</h2>
      <div className={styles['project-text']}>
        <form>
          <h3>-INSERT PROJECT NAME HERE- created</h3>
        </form>
          <h3>Share the code with your project members: </h3>
        {/* <input type="submit" name="taskBoardButton" placeholder = "Go to ProjectTask Board"></input> */}
        </div>
    </div>
  );
}
