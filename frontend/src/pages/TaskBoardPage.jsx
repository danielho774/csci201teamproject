import React, { useState, useEffect, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './TaskBoardPage.module.css';

const STATUSES = ['Incomplete', 'In Progress', 'Complete'];

export default function TaskBoardPage() {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([]);
  const STATUS_ID_MAP = {
    'Incomplete': 1,
    'In Progress': 2,
    'Complete': 3,
  };

  const userID = localStorage.getItem("userID");
  const [userName, setUserName] = useState("");
  const [projectName, setProjectName] = useState("");

  useEffect(() => {
    const fetchProjectName = async () => {
      try {
        const response = await fetch(`/api/projects/${projectId}`);
        if (response.ok) {
          const data = await response.json();
          setProjectName(data.projectName);
        } else {
          console.error('Failed to fetch project name');
        }
      } catch (err) {
        console.error('Error fetching project:', err);
      }
    };

    fetchProjectName();
  }, [projectId]);

  useEffect(() => {
    const fetchUserInfo = async () => {
      if (!userID) return;
      try {
        const response = await fetch(`/api/users/${userID}`);
        if (response.ok) {
          const userData = await response.json();
          setUserName(`${userData.firstName} ${userData.lastName}`);
        }
      } catch (err) {
        console.error('Failed to fetch user info:', err);
      }
    };
    fetchUserInfo();
  }, [userID]);

  const fetchTasks = async () => {
    try {
      console.log('Fetching tasks for project:', projectId);
      const response = await fetch(`/api/tasks/project/${projectId}`);
      if (!response.ok) {
        console.error('Failed to fetch tasks:', response.status, response.statusText);
        return;
      }
      const data = await response.json();
      console.log('Fetched tasks:', data);
      if (data && data.length > 0) {
        setTasks(data);
      }
    } catch (err) {
      console.error('Failed to fetch project tasks:', err);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, [projectId]);

  const progress = useMemo(() => {
    if (!tasks.length) return 0;
    const done = tasks.filter(t => t.statusName === 'Complete').length;
    return Math.round((done / tasks.length) * 100);
  }, [tasks]);

  const handleStatusChange = async (taskId, newStatusName) => {
    const statusID = STATUS_ID_MAP[newStatusName];
    console.log(`Updating task ${taskId} status to: ${newStatusName} (ID: ${statusID})`);
  
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
  
      const responseData = await res.text();
      console.log('Status update response:', responseData);
      
      if (!res.ok) {
        console.error('Failed to update task status:', responseData);
        fetchTasks();
      }
    } catch (error) {
      console.error('Network error while updating status:', error);
      fetchTasks();
    }
  };

  const handleClaimTask = async (task) => {
    if (task.assigned && task.assignedUserId !== parseInt(userID)) {
      console.log("This task is already claimed by another user");
      return;
    }

    const isClaiming = !task.assigned || task.assignedUserId !== parseInt(userID);
  
    const endpoint = `/api/tasks/${task.taskID}/${isClaiming ? "assign" : "remove"}/${userID}`;
    const method = isClaiming ? "POST" : "DELETE";
  
    try {
      console.log(`${isClaiming ? 'Claiming' : 'Unclaiming'} task ${task.taskID}`);
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
          body: JSON.stringify({ statusID: 2 }),
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
                assigned: isClaiming,
                assignedUserId: isClaiming ? parseInt(userID) : -1,
                statusID: isClaiming ? 2 : t.statusID,
                statusName: isClaiming ? 'In Progress' : t.statusName,
              }
            : t
        )
      );

      fetchTasks();
    } catch (err) {
      console.error("Error while claiming task:", err);
      fetchTasks();
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

  const getClaimButtonText = (task) => {
    if (!task.assigned) {
      return 'Claim Task';
    }
    
    if (task.assignedUserId === parseInt(userID)) {
      return 'Unclaim Task';
    }
    
    return 'Already Claimed';
  };

  const isClaimButtonDisabled = (task) => {
    return task.assigned && task.assignedUserId !== parseInt(userID);
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
        <h2>Task Board for Project: {[projectName]}</h2>
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
            {userID && <th>Assignment</th>}
            {userID && <th>Actions</th>}
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
                  disabled={!userID}
                >
                  {STATUSES.map(s => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </select>
              </td>
              {userID && (
                <td>
                  <button 
                    className={`${styles.taskButton} ${task.assignedUserId === parseInt(userID) ? styles.unclaim : styles.claim} ${isClaimButtonDisabled(task) ? styles.disabled : ''}`} 
                    onClick={() => handleClaimTask(task)}
                    disabled={isClaimButtonDisabled(task)}
                  >
                    {getClaimButtonText(task)}
                  </button>
                </td>
              )}
              {userID && (
                <td>
                  <button 
                    className={`${styles.taskButton} ${styles.delete}`}
                    onClick={() => handleDeleteTask(task.taskID)}
                  >
                    Delete
                  </button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
      
      {userID && (
        <button
          className={styles.addButton}
          onClick={() => navigate(`/projects/${projectId}/tasks/add`)}
        >
          + Add New Task
        </button>
      )}
    </div>
  );
}

