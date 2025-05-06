import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';

import ProjectHub from './pages/ProjectHubPage';
import IndividualTasks from './pages/IndividualTasksPage'
import CreateProjectPage from './pages/CreateProjectPage';
import JoinProjectPage from './pages/JoinProjectPage';
import ProjectConfirmedPage from './pages/ProjectConfirmedPage';
import AvailabilityPage from './pages/AvailabilityPage';
import {LoginSignup} from './components/LoginSignup';
import RegisterPage from './pages/RegisterPage';
import TaskBoardPage from './pages/TaskBoardPage';
import AddTaskPage from './pages/AddTaskPage';
import ProjectCard from './components/ProjectCard';
import NavigationBar from './components/NavigationBar';
import styles from './components/NavigationBar.module.css';

function showProjectsList(projectItem){
  return(
    <ProjectCard
      key = {projectItem.key}
      project-title = {projectItem['project-title']}
      project-info = {projectItem['project-info']}
    />
  );
}

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    localStorage.clear();
  };

  return (
    <Router>
      <NavigationBar isLoggedIn={isLoggedIn} onLogout={handleLogout} />
      <div className={styles.mainContent}>
        <Routes>
          <Route path="/" element={<ProjectHub />} />
          <Route path="/tasks" element={<IndividualTasks />} />
          <Route path="/create" element={<CreateProjectPage />} />
          <Route path="/join" element={<JoinProjectPage />} />
          <Route path="/confirmed" element={<ProjectConfirmedPage />} />
          <Route path="/availability" element={<AvailabilityPage />} />
          <Route path="/login" element={<LoginSignup onLogin={handleLogin} />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/projects/:projectId/board" element={<TaskBoardPage />} />
          <Route path="/projects/:projectId/tasks/add" element={<AddTaskPage />} />
        </Routes>
    </div>
    </Router>
  );
}

export default App;
