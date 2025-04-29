
import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './AddTaskPage.module.css';

export default function AddTaskPage() {
  const { projectId } = useParams();
  const [title, setTitle]   = useState('');
  const navigate            = useNavigate();

  function handleSubmit(e) {
    e.preventDefault();
    if (!title.trim()) return;
    navigate(`/projects/${projectId}/board`);
  }

  return (
    <div className={styles.container}>
      <h2>Add Task to Project {projectId}</h2>
      <form onSubmit={handleSubmit}>
        <input
          className={styles.input}
          value={title}
          onChange={e => setTitle(e.target.value)}
          placeholder="Task title"
        />
        <button type="submit" className={styles.button}>
          Create Task
        </button>
      </form>
    </div>
  );
}

