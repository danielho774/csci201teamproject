import React from 'react';
import styles from './IndividualTasksPage.module.css';

import TaskCard from '../components/TaskCard';

export default function IndividualTasksPage() {
  return (
    <div className={styles['user-page']}>
      <h2>My Tasks</h2>
      <TaskCard task-title = "Task1" />
     
    </div>
  );
}
