import React, { useState, useMemo } from 'react';
import styles from './AvailabilityPage.module.css';

const DAYS = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
const TIMES = [
  '8 AM','9 AM','10 AM','11 AM','12 PM',
  '1 PM','2 PM','3 PM','4 PM','5 PM','6 PM'
];

const SAMPLE = [
  { user: 'Alice', slots: ['Mon-9 AM','Tue-2 PM','Thu-1 PM'] },
  { user: 'Bob',   slots: ['Mon-9 AM','Wed-11 AM','Fri-4 PM'] },
];

export default function AvailabilityPage() {
  const [currentDate, setCurrentDate] = useState(new Date());


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
    SAMPLE.forEach(({ user, slots }) => {
      slots.forEach(s => {
        m[s] = m[s] || [];
        m[s].push(user);
      });
    });
    return m;
  }, []);

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <h1>{weekTitle}</h1>
      </div>

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
              const isHover = false; 

              return (
                <div
  key={slotKey}
  className={styles.cell}
>

  {people.length > 0 && (
    <div className={styles.initials}>
      {people.map(u => u[0]).join('')}
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


