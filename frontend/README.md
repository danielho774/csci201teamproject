my-project/
├── public/                        
│   └── index.html                 // HTML template; the root HTML page that loads the React app
├── src/
│   ├── components/                // Reusable UI components (e.g., buttons, input fields, navigation bar, etc.)
│   │   ├── NavigationSidebar.jsx  // Component for the left navigation sidebar
│   │   └── TaskCard.jsx           // Component representing an individual task card
│   ├── pages/                     // Page components corresponding to your application's different pages
│   │   ├── LoginPage.jsx          // Main login page for existing users (includes email and password inputs)
│   │   ├── RegisterPage.jsx       // Registration page for new users (includes fields for account creation, availability, and preferences)
│   │   ├── TaskBoardPage.jsx      // Task board page that displays all tasks and an overall progress bar
│   │   ├── CreateProjectPage.jsx  // Page for creating a new project (with project name, duration, and optional task details)
│   │   ├── AddTaskPage.jsx        // Page for adding a new task (includes task name, duration, and due date)
│   │   ├── IndividualTasksPage.jsx// Page displaying the current user's tasks with options to update status and view comments
│   │   └── AvailabilityPage.jsx   // Page for scheduling user availability (a When2Meet-style interface)
│   ├── services/                  // Service layer for API calls (handles communication with the backend)
│   │   └── api.js                 // API module to process network requests (e.g., login, register, fetch tasks, etc.)
│   ├── context/                   // Global state management; using Context API or Redux (optional)
│   │   └── AuthContext.jsx        // Context for managing authentication state (user login status, token, etc.)
│   ├── App.jsx                    // Main application component with routing and global layout
│   └── index.js                   // Application entry point where the React app is rendered into the DOM
└── package.json                   // Project configuration and dependency management file
