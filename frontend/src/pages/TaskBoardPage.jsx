import React, { useState, useEffect, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './TaskBoardPage.module.css';

const STATUSES = ['Not Started', 'In Progress', 'Completed'];

export default function TaskBoardPage() {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([]);
  const STATUS_ID_MAP = {
    'Not Started': 0,
    'In Progress': 1,
    'Completed': 2,
  };

  useEffect(() => {
    fetch(`/api/tasks/project/${projectId}`)
      .then(res => res.json())
      .then(data => {
        if (data && data.length > 0) {
          setTasks(data);
        }
      })
      .catch((err) => {
        console.error('Failed to fetch project tasks:', err);
      });
  }, [projectId]);

  const progress = useMemo(() => {
    if (!tasks.length) return 0;
    const done = tasks.filter(t => t.statusName === 'Completed').length;
    return Math.round((done / tasks.length) * 100);
  }, [tasks]);

  const handleStatusChange = async (taskId, newStatusName) => {
    const statusID = STATUS_ID_MAP[newStatusName];
  
    setTasks(ts =>
      ts.map(t =>
        t.taskID === taskId ? { ...t, statusName: newStatusName, statusID } : t
      )
    );
  
    try {
      const res = await fetch(`/api/tasks/${taskId}/status`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ statusID }), 
      });
  
      if (!res.ok) {
        const msg = await res.text();
        console.error('Failed to update task status:', msg);
      }
    } catch (error) {
      console.error('Network error while updating status:', error);
    }
  };

  const userID = localStorage.getItem("userID");

  const handleClaimTask = async (task) => {
    const isClaiming = task.assigned != userID;
  
    const endpoint = `/api/tasks/${task.taskID}/${isClaiming ? "assign" : "remove"}/${userID}`;
    const method = isClaiming ? "POST" : "DELETE";
  
    try {
      const response = await fetch(endpoint, { method });
  
      if (!response.ok) {
        const error = await response.text();
        console.error("Server error:", error);
        return;
      }

      if (isClaiming) {
        const statusUpdate = await fetch(`/api/tasks/${task.taskID}/status`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({ statusID: 1 }),
        });
  
        if (!statusUpdate.ok) {
          const error = await statusUpdate.text();
          console.error("Failed to update status:", error);
        }
      }

      setTasks(prevTasks =>
        prevTasks.map(t =>
          t.taskID === task.taskID
            ? {
                ...t,
                assigned: isClaiming ? userID : null,
                statusID: isClaiming ? 1 : t.statusID,
                statusName: isClaiming ? 'In Progress' : t.statusName,
              }
            : t
        )
      );
    } catch (err) {
      console.error("Error while claiming task:", err);
    }
  };

  const handleDeleteTask = async (taskId) => {
    try {
      const response = await fetch(`/api/tasks/${taskId}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        setTasks(tasks.filter(t => t.taskID !== taskId));
      } else {
        console.error('Failed to delete task:', await response.text());
      }
    } catch (error) {
      console.error('Error while deleting task:', error);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <button 
          className={styles.backButton}
          onClick={() => navigate('/')}
        >
          Back to Project Hub
        </button>
        <h2>Task Board for Project: {projectId}</h2>
      </div>

      <div className={styles.progressBar}>
        <label>Overall Progress: {progress}%</label>
        <progress value={progress} max="100" />
      </div>
      <table className={styles.table}>
        <thead>
          <tr>
            <th>Task</th>
            <th>Description</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Status</th>
            <th>Assignment</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map(task => (
            <tr key={task.taskID}>
              <td>{task.taskName}</td>
              <td>{task.description}</td>
              <td>{task.startDate}</td>
              <td>{task.endDate}</td>
              <td>
                <select
                  value={task.statusName}
                  onChange={e => handleStatusChange(task.taskID, e.target.value)}
                  className={styles.statusSelect}
                >
                  {STATUSES.map(s => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </td>
              <td>
                <button 
                  className={`${styles.taskButton} ${task.assigned == userID ? styles.unclaim : styles.claim}`} 
                  onClick={() => handleClaimTask(task)}
                >
                  {task.assigned != userID ? 'Claim Task' : 'Unclaim Task'}
                </button>
              </td>
              <td>
                <button 
                  className={`${styles.taskButton} ${styles.delete}`}
                  onClick={() => handleDeleteTask(task.taskID)}
                >
                  Delete
                </button>
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

