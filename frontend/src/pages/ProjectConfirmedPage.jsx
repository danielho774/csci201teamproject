import React from 'react';
import styles from './CreateProjectPage.module.css';

import ShareCodeCard from '../components/ShareCodeCard';

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
        <ShareCodeCard code = "123456" />

        
        </div>
    </div>
  );
}
