import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './AddTaskPage.module.css';

export default function AddTaskPage() {
  const { projectId } = useParams();
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [error, setError] = useState('');

  async function handleSubmit(e) {
    e.preventDefault();

    if (!title.trim()) {
      setError("Task title is required");
      return;
    }

    const newTask = {
      taskName: title,
      taskDescrip: description,
      startDate: startDate,
      endDate: endDate,
      project: { projectID: Number(projectId) },
      status: { statusID: 1 },
    };

    try {
      const res = await fetch('http://localhost:8080/api/tasks', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newTask),
      });

      if (res.ok) {
        navigate(`/projects/${projectId}/board`);
      } else {
        const data = await res.json();
        setError(data.message || 'Failed to create task');
      }
    } catch (err) {
      console.error('Error creating task:', err);
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.formWrapper}>
        <h2 className={styles.heading}>Add Task to Project {projectId}</h2>
        {error && <p className={styles.error}>{error}</p>}
        <form onSubmit={handleSubmit} className={styles.form}>
          <input
            className={styles.input}
            value={title}
            onChange={e => setTitle(e.target.value)}
            placeholder="Task title"
          />
          <textarea
            className={styles.textarea}
            value={description}
            onChange={e => setDescription(e.target.value)}
            placeholder="Task description"
            rows={4}
          />
          <div className={styles.dateRow}>
            <label className={styles.label}>
              Start Date
              <input
                className={styles.input}
                type="date"
                value={startDate}
                onChange={e => setStartDate(e.target.value)}
              />
            </label>
            <label className={styles.label}>
              End Date
              <input
                className={styles.input}
                type="date"
                value={endDate}
                onChange={e => setEndDate(e.target.value)}
              />
            </label>
          </div>
          <button type="submit" className={styles.submitButton}>
            Create Task
          </button>
        </form>
      </div>
    </div>
  );
}