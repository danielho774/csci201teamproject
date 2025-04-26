import React from 'react';
import styles from './ShareCodeCard.module.css';


function ShareCodeCard(props) {
    return (
        <div className = {styles['wrapper']}>
            <div className = {styles['share-code-card']}>
                <h1 className = {styles['code']}>{props['code']}</h1>
            </div>
        </div>
    )
}

export default ShareCodeCard; 