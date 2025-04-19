import React from 'react';

import ProjectHub from './pages/ProjectHubPage';
import IndividualTasks from './pages/IndividualTasksPage'
import CreateProjectPage from './pages/CreateProjectPage';
import ProjectConfirmedPage from './pages/ProjectConfirmedPage';

function showProjectsList(projectItem){
  return(
    <ProjectCard
      key = {projectItem.key}
      project-title = {projectItem.project-title}
      project-info = {projectItem.project-info}
    />
  );
}
/*Note: the above is unused right now*/

function App() {
  return (
    <div>
      <h1>Hello React!</h1>
      <ProjectHub/>
      <IndividualTasks/>
      <CreateProjectPage/>
      <ProjectConfirmedPage/>

      {/* {projects.map(showProjectsList)} */}

    </div>
  );
}

export default App;
