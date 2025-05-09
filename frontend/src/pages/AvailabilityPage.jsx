import React, { useState, useMemo, useEffect } from 'react';
import styles from './AvailabilityPage.module.css';

const DAYS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
const TIMES = [
  '8 AM','9 AM','10 AM','11 AM','12 PM',
  '1 PM','2 PM','3 PM','4 PM','5 PM','6 PM'
];

export default function AvailabilityPage() {
  const [currentDate, setCurrentDate] = useState(new Date());
  const [showInputForm, setShowInputForm] = useState(false);
  const [selectedDay, setSelectedDay] = useState('Mon');
  const [selectedStartTime, setSelectedStartTime] = useState('9 AM');
  const [selectedEndTime, setSelectedEndTime] = useState('10 AM');
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState(null);
  const [availabilities, setAvailabilities] = useState([]);
  const [userName, setUserName] = useState('');
  const [allUsers, setAllUsers] = useState({});

  // Fetch user's projects
  useEffect(() => {
    const fetchUserProjects = async () => {
      try {
        const userID = localStorage.getItem('userID');
        const response = await fetch(`http://localhost:8080/api/users/${userID}/projects`);
        if (response.ok) {
          const data = await response.json();
          setProjects(Array.isArray(data) ? data : [data]);
          if (data.length > 0) {
            setSelectedProject(data[0].projectID); // Select first project by default
          }
        }
      } catch (error) {
        console.error('Failed to fetch projects:', error);
      }
    };
    fetchUserProjects();
  }, []);

  // Fetch availabilities when selected project changes
  useEffect(() => {
    const fetchAvailabilities = async () => {
      if (!selectedProject) return;
      
      try {
        const response = await fetch(`http://localhost:8080/api/availability/get/all/project/${selectedProject}`, {
          headers: {
            'Accept': 'application/json'
          }
        });
        
        if (response.status === 404) {
          setAvailabilities([]);
          return;
        }

        if (!response.ok) {
          console.error('Failed to fetch availabilities:', await response.text());
          return;
        }

        const data = await response.json();
        setAvailabilities(data);
        
        // Fetch ALL users' names who have availabilities
        const userIds = [...new Set(data.map(a => a.userID))];
        const userDetails = {};
        
        for (const userId of userIds) {
          const userResponse = await fetch(`http://localhost:8080/api/users/${userId}`, {
            headers: {
              'Accept': 'application/json'
            }
          });
          
          if (userResponse.ok) {
            const userData = await userResponse.json();
            userDetails[userId] = `${userData.firstName} ${userData.lastName}`;
          }
        }
        
        setAllUsers(userDetails);
      } catch (error) {
        console.error('Failed to fetch availabilities:', error);
      }
    };

    fetchAvailabilities();
  }, [selectedProject]);

  // Add useEffect to fetch user's name
  useEffect(() => {
    const fetchUserName = async () => {
      const userID = localStorage.getItem('userID');
      try {
        const response = await fetch(`http://localhost:8080/api/users/${userID}`);
        if (response.ok) {
          const user = await response.json();
          setUserName(`${user.firstName} ${user.lastName}`);
        }
      } catch (error) {
        console.error('Failed to fetch user name:', error);
      }
    };
    fetchUserName();
  }, []);

  const weekStart = useMemo(() => {
    const d = new Date(currentDate);
    const day = d.getDay();
    const diff = day === 0 ? 6 : day - 1;
    return new Date(d.getFullYear(), d.getMonth(), d.getDate() - diff);
  }, [currentDate]);


  const prevWeek = () =>
    setCurrentDate(d =>
      new Date(d.getFullYear(), d.getMonth(), d.getDate() - 7)
    );
  const nextWeek = () =>
    setCurrentDate(d =>
      new Date(d.getFullYear(), d.getMonth(), d.getDate() + 7)
    );


  const weekDates = useMemo(() => {
    const arr = [];
    for (let i = 0; i < 7; i++) {
      const d = new Date(weekStart);
      d.setDate(weekStart.getDate() + i);
      arr.push(d);
    }
    return arr;
  }, [weekStart]);


  const fmtMD = d =>
    d.toLocaleDateString('default',
      { month: 'short', day: 'numeric' }
    );


  const weekTitle =
    `${fmtMD(weekDates[0])} – ${fmtMD(weekDates[6])}, ${weekDates[6].getFullYear()}`;


  const slotMap = useMemo(() => {
    const m = {};
    availabilities.forEach(avail => {
      const date = new Date(avail.date);
      let day = date.getDay() - 1;
      if (day === -1) day = 6;
      const dayName = DAYS[day];
      
      const getHour = (timeStr) => {
        const [hours] = timeStr.split(':');
        return parseInt(hours);
      };
      
      const startHour = getHour(avail.startTime);
      const endHour = getHour(avail.endTime);
      
      for (let hour = startHour; hour < endHour; hour++) {
        const timeStr = `${hour % 12 || 12} ${hour < 12 ? 'AM' : 'PM'}`;
        const slotKey = `${dayName}-${timeStr}`;
        m[slotKey] = m[slotKey] || [];
        
        const userName = allUsers[avail.userID];
        if (userName && !m[slotKey].includes(userName)) {
          m[slotKey].push(userName);
        }
      }
    });
    
    return m;
  }, [availabilities, allUsers]);

  const handleSubmit = async () => {
    if (!selectedProject) {
      console.error('No project selected');
      return;
    }

    const userID = localStorage.getItem('userID');
    
    const selectedDayIndex = DAYS.indexOf(selectedDay);
    const date = new Date(weekStart);
    date.setDate(date.getDate() + selectedDayIndex);
    const formattedDate = date.toISOString().split('T')[0];

    const convertTime = (timeStr) => {
      const [time, period] = timeStr.split(' ');
      let hour = parseInt(time);
      if (period === 'PM' && hour !== 12) hour += 12;
      if (period === 'AM' && hour === 12) hour = 0;
      return `${hour.toString().padStart(2, '0')}:00:00`;
    };

    try {
      const response = await fetch(`http://localhost:8080/api/availability/add/user/${userID}/project/${selectedProject}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        body: JSON.stringify({
          date: formattedDate,
          startTime: convertTime(selectedStartTime),
          endTime: convertTime(selectedEndTime)
        })
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error('Failed to add availability:', errorText);
        return;
      }

      setShowInputForm(false);

      // Refresh ALL availabilities for the project
      const updatedResponse = await fetch(`http://localhost:8080/api/availability/get/all/project/${selectedProject}`, {
        headers: {
          'Accept': 'application/json'
        }
      });

      if (updatedResponse.ok) {
        const data = await updatedResponse.json();
        setAvailabilities(data);
        
        // Update user names if there's a new user
        const newUserIds = [...new Set(data.map(a => a.userID))];
        const existingUserIds = Object.keys(allUsers).map(id => parseInt(id));
        const newUsers = newUserIds.filter(id => !existingUserIds.includes(id));
        
        if (newUsers.length > 0) {
          const userDetails = { ...allUsers };
          for (const userId of newUsers) {
            const userResponse = await fetch(`http://localhost:8080/api/users/${userId}`, {
              headers: {
                'Accept': 'application/json'
              }
            });
            
            if (userResponse.ok) {
              const userData = await userResponse.json();
              userDetails[userId] = `${userData.firstName} ${userData.lastName}`;
            }
          }
          setAllUsers(userDetails);
        }
      }
    } catch (error) {
      console.error('Error adding availability:', error);
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <h1>{weekTitle}</h1>
        <div className={styles.controls}>
          <select 
            value={selectedProject || ''} 
            onChange={(e) => setSelectedProject(Number(e.target.value))}
            className={styles.projectSelect}
          >
            <option value="">Select a Project</option>
            {projects.map(project => (
              <option key={project.projectID} value={project.projectID}>
                {project.projectName}
              </option>
            ))}
          </select>
          <button 
            onClick={() => setShowInputForm(!showInputForm)} 
            className={styles.availabilityButton}
            disabled={!selectedProject}
          >
            {showInputForm ? 'Cancel' : '+ Add Availability'}
          </button>
        </div>
      </div>

      {showInputForm && (
        <div className={styles.inputForm}>
          <div className={styles.formRow}>
            <label>Day:</label>
            <select 
              value={selectedDay} 
              onChange={(e) => setSelectedDay(e.target.value)}
            >
              {DAYS.map(day => (
                <option key={day} value={day}>{day}</option>
              ))}
            </select>
          </div>
          
          <div className={styles.formRow}>
            <label>From:</label>
            <select 
              value={selectedStartTime} 
              onChange={(e) => setSelectedStartTime(e.target.value)}
            >
              {TIMES.map(time => (
                <option key={time} value={time}>{time}</option>
              ))}
            </select>
          </div>
          
          <div className={styles.formRow}>
            <label>To:</label>
            <select 
              value={selectedEndTime} 
              onChange={(e) => setSelectedEndTime(e.target.value)}
            >
              {TIMES.map(time => (
                <option key={time} value={time}>{time}</option>
              ))}
            </select>
          </div>
          
          <button onClick={handleSubmit} className={styles.submitButton}>
            Submit
          </button>
        </div>
      )}

      <div className={styles.weekBar}>
        <button onClick={prevWeek} className={styles.arrow}>&lt;</button>
        <span className={styles.weekLabel}>{weekTitle}</span>
        <button onClick={nextWeek} className={styles.arrow}>&gt;</button>
      </div>

      <div className={styles.calendar}>
        <div className={styles.weekdays}>
          {weekDates.map(d => (
            <div key={d.toISOString()} className={styles.weekday}>
              <div className={styles.weekdayName}>
                {DAYS[d.getDay()]}
              </div>
              <div className={styles.weekdayDate}>
                {d.getDate()}
              </div>
            </div>
          ))}
        </div>

        <div className={styles.times}>
          {TIMES.map(t => (
            <div key={t} className={styles.timeLabel}>{t}</div>
          ))}
        </div>

        <div className={styles.grid}>
          {TIMES.map(t =>
            DAYS.map((_, i) => {
              const slotKey = `${DAYS[i]}-${t}`;
              const people = slotMap[slotKey] || [];

              return (
                <div
                  key={slotKey}
                  className={styles.cell}
                >
                  {people.length > 0 && (
                    <div className={styles.initials}>
                      {people.map(name => name.split(' ').map(n => n[0]).join('')).join(', ')}
                    </div>
                  )}

                  {people.length > 0 && (
                    <div className={styles.tooltip}>
                      {people.join(', ')}
                    </div>
                  )}
                </div>
              );
            })
          )}
        </div>
      </div>
    </div>
  );
}


