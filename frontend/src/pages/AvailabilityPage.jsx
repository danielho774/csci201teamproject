import React, { useState, useMemo, useEffect } from 'react';
import styles from './AvailabilityPage.module.css';

const DAYS = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
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
        
        // Don't treat 404 as an error, just set empty availabilities
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
        
        // Fetch current user's name immediately
        const userID = localStorage.getItem('userID');
        const userResponse = await fetch(`http://localhost:8080/api/users/${userID}`, {
          headers: {
            'Accept': 'application/json'
          }
        });
        
        if (userResponse.ok) {
          const userData = await userResponse.json();
          setAllUsers(prev => ({
            ...prev,
            [userID]: `${userData.firstName} ${userData.lastName}`
          }));
        }
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
    return new Date(d.getFullYear(),
                    d.getMonth(),
                    d.getDate() - day);
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
      // Convert the date string to a Date object
      const date = new Date(avail.date);
      const day = DAYS[date.getDay()];
      
      // Convert time strings from "HH:mm:ss" to hour number
      const getHour = (timeStr) => {
        const [hours] = timeStr.split(':');
        return parseInt(hours);
      };
      
      const startHour = getHour(avail.startTime);
      const endHour = getHour(avail.endTime);
      
      // Add entries for each hour in the time range
      for (let hour = startHour; hour < endHour; hour++) {
        const timeStr = `${hour % 12 || 12} ${hour < 12 ? 'AM' : 'PM'}`;
        const slotKey = `${day}-${timeStr}`;
        m[slotKey] = m[slotKey] || [];
        
        // Add user name if we have it
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
    const date = weekDates[DAYS.indexOf(selectedDay)].toISOString().split('T')[0];
    
    // Convert time format from "H AM/PM" to "HH:mm:ss"
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
          date: date,
          startTime: convertTime(selectedStartTime),
          endTime: convertTime(selectedEndTime)
        })
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error('Failed to add availability:', errorText);
        return;
      }

      // Get the newly added availability
      const newAvailability = await response.json();
      
      // Update the availabilities state immediately
      setAvailabilities(prev => [...prev, newAvailability]);
      setShowInputForm(false);

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


