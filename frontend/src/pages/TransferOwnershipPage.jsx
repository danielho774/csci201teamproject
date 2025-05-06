import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom'; 
import styles from './JoinProjectPage.module.css';

export default function TransferOwnershipPage({ currentUserId }) {
  const [members, setMembers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { projectID } = useParams(); // Assuming route provides projectID

  useEffect(() => {
    const fetchMembers = async () => {
      try {
        const response = await fetch(`/api/projects/${projectID}/members`);
        const data = await response.json();

        // Filter out the current user
        const filteredMembers = data.filter(member => member.id !== currentUserId);
        setMembers(filteredMembers);
        setLoading(false);
      } catch (error) {
        setErrorMessage('Failed to load project members.');
        setLoading(false);
      }
    };

    fetchMembers();
  }, [projectID, currentUserId]);

  const handleSubmit = async () => {
    if (!selectedUserId) {
      setErrorMessage('Please select a member to transfer ownership.');
      return;
    }

    try {
      const response = await fetch(`/api/projects/${projectID}/transfer-ownership`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ newOwnerId: selectedUserId }),
      });

      if (!response.ok) throw new Error('Transfer failed');

      navigate(`/projects/${projectID}`);
    } catch (error) {
      setErrorMessage('Error transferring ownership.');
    }
  };

  return (
    <div className={styles['join-project']}>
      <h2>Transfer Ownership</h2>

      {loading ? (
        <p>Loading members...</p>
      ) : (
        <>
          {members.length === 0 ? (
            <p>No other members to transfer ownership to.</p>
          ) : (
            <div className={styles['input-wrapper']}>
              {members.map(member => (
                <label key={member.id} className={styles['member-option']}>
                  <input
                    type="radio"
                    name="transferTarget"
                    value={member.id}
                    checked={selectedUserId === member.id}
                    onChange={() => setSelectedUserId(member.id)}
                  />
                  {member.name}
                </label>
              ))}
              {errorMessage && <p className={styles['error']}>{errorMessage}</p>}
              <button className={styles['submit-button']} onClick={handleSubmit}>
                Confirm Transfer
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
}
