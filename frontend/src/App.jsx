import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom';

import ProjectHub from './pages/ProjectHubPage';
import CreateProjectPage from './pages/CreateProjectPage';
import JoinProjectPage from './pages/JoinProjectPage';
import AvailabilityPage from './pages/AvailabilityPage';
import {LoginSignup} from './components/LoginSignup';
import RegisterPage from './pages/RegisterPage';
import TaskBoardPage from './pages/TaskBoardPage';
import AddTaskPage from './pages/AddTaskPage';
import ProjectCard from './components/ProjectCard';
import NavigationBar from './components/NavigationBar';
import styles from './components/NavigationBar.module.css';
import TransferOwnershipPage from './pages/TransferOwnershipPage';
import Logout from './components/Logout';

// function showProjectsList(projectItem){
//   return(
//     <ProjectCard
//       key = {projectItem.key}
//       project-title = {projectItem['project-title']}
//       project-info = {projectItem['project-info']}
//     />
//   );
// }

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(localStorage.getItem('logged-in') === 'true');

  const handleLogin = () => {
    setIsLoggedIn(true);
    localStorage.setItem('logged-in', 'true');
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
          <Route path="/create" element={<CreateProjectPage />} />
          <Route path="/join" element={<JoinProjectPage />} />
          <Route path="/availability" element={<AvailabilityPage />} />
          <Route path="/login" element={<LoginSignup onLogin={handleLogin} />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/transfer" element={<TransferOwnershipPage />} />
          <Route path="/projects/:projectId/board" element={<TaskBoardPage />} />
          <Route path="/projects/:projectId/tasks/add" element={<AddTaskPage />} />
          <Route path="/projects/:projectID/ownership" element={<TransferOwnershipPage />}  />
          <Route path="/logout" element={<Logout setIsLoggedIn={setIsLoggedIn} />} />
        </Routes>
    </div>
    </Router>
  );
}

export default App;
