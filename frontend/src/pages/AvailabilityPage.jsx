
import React, { useState } from 'react'
import styles from './AvailabilityPage.module.css'

const DAYS = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat']
const TIMES = [
  '8 AM','9 AM','10 AM','11 AM','12 PM',
  '1 PM','2 PM','3 PM','4 PM','5 PM','6 PM'
]

export default function AvailabilityPage() {
  
  const [date, setDate] = useState(new Date())
  const monthName = date.toLocaleString('default',{ month: 'long' })
  const year = date.getFullYear()

  const prevMonth = () =>
    setDate(d => new Date(d.getFullYear(), d.getMonth() - 1, 1))
  const nextMonth = () =>
    setDate(d => new Date(d.getFullYear(), d.getMonth() + 1, 1))

  return (
    <div className={styles.page}>
      {/* Header + Title */}
      <div className={styles.header}>
        <h1>Calendar Page</h1>
      </div>

      {/* Month selector bar */}
      <div className={styles.monthBar}>
        <button onClick={prevMonth} className={styles.arrow}>&lt;</button>
        <span className={styles.monthLabel}>
          {monthName} {year}
        </span>
        <button onClick={nextMonth} className={styles.arrow}>&gt;</button>
      </div>

      {/* Calendar grid */}
      <div className={styles.calendar}>
        {/* Weekday row */}
        <div className={styles.weekdays}>
          {DAYS.map(d => (
            <div key={d} className={styles.weekday}>{d}</div>
          ))}
        </div>

        {/* Time labels down the left */}
        <div className={styles.times}>
          {TIMES.map(t => (
            <div key={t} className={styles.timeLabel}>{t}</div>
          ))}
        </div>

        {/* The actual grid of cells */}
        <div className={styles.grid}>
          {TIMES.map(t =>
            DAYS.map(d => (
              <div key={`${d}-${t}`} className={styles.cell}>
                {/* You could shade this cell if user X is available */}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  )
}

