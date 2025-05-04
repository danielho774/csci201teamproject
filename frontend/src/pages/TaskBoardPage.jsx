import React, { useState, useEffect, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './TaskBoardPage.module.css';

const STATUSES = ['Not Started', 'In Progress', 'Completed'];

export default function TaskBoardPage() {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([
    { id: 1, title: 'Filler Task', status: 'Not Started' }
  ]);

  useEffect(() => {
    fetch(`/api/tasks`)
      .then(res => res.json())
      .then(data => {
        if (data && data.length > 0) {
          setTasks(data);
        }
      })
      .catch(() => {
        setTasks([
          { id: 1, title: 'Filler Task', status: 'Not Started' }
        ]);
      });
  }, [projectId]);

  const progress = useMemo(() => {
    if (!tasks.length) return 0;
    const done = tasks.filter(t => t.status === 'Completed').length;
    return Math.round((done / tasks.length) * 100);
  }, [tasks]);

  const handleStatusChange = async (id, newStatus) => {
    setTasks(ts =>
      ts.map(t => (t.id === id ? { ...t, status: newStatus } : t))
    );

    try {
      await fetch(`/api/tasks/${id}/status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ status: newStatus }),
      });
    } catch (error) {
      console.error('Failed to update task status:', error);
    }
  };

  return (
    <div className={styles.container}>
      <h2>Task Board for Project: {projectId}</h2>

      <div className={styles.progressBar}>
        <label>Overall Progress: {progress}%</label>
        <progress value={progress} max="100" />
      </div>
      <table className={styles.table}>
        <thead>
          <tr>
            <th>Task</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map(task => (
            <tr key={task.id}>
              <td>{task.title}</td>
              <td>
                <select
                  value={task.status}
                  onChange={e => handleStatusChange(task.id, e.target.value)}
                  className={styles.statusSelect}
                >
                  {STATUSES.map(s => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button
        className={styles.addButton}
        onClick={() => navigate(`/projects/${projectId}/tasks/add`)}
      >
        + Add New Task
      </button>
    </div>
  );
}

