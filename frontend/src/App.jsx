import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import ProjectHub from './pages/ProjectHubPage';
import IndividualTasks from './pages/IndividualTasksPage'
import CreateProjectPage from './pages/CreateProjectPage';
import ProjectConfirmedPage from './pages/ProjectConfirmedPage';
import AvailabilityPage from './pages/AvailabilityPage';
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
  return (
    <Router>
      <NavigationBar />
      <div className={styles.mainContent}>
        <Routes>
          <Route path="/" element={<ProjectHub />} />
          <Route path="/tasks" element={<IndividualTasks />} />
          <Route path="/create" element={<CreateProjectPage />} />
          <Route path="/confirmed" element={<ProjectConfirmedPage />} />
          <Route path="/availability" element={<AvailabilityPage />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
