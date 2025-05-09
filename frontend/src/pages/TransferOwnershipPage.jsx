import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom'; 
import styles from './TransferOwnershipPage.module.css';
import { useLocation } from 'react-router-dom'; 

export default function TransferOwnershipPage() {
  const [members, setMembers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const {projectID } = useParams(); // Assuming route provides projectID

  const location = useLocation(); 
  const currentUserId = location.state?.currentUserId; 

  // console.log('Current User ID: ', currentUserId); 

  useEffect(() => {
    const fetchMembers = async () => {
      try {
        // console.log('Check projectID: ', projectID); 
        const response = await fetch(`/api/projects/${projectID}/members`);
        const data = await response.json();
        // console.log('Members: ', data); 

        // data.forEach(member => {
        //   console.log(`MemberID: ${member.memberID}, isOwner: ${member.role}`)
        // })
        // Filter out the current user
        const filteredMembers = data.filter(member => !member.role);
        // console.log('Filtered Members: ', filteredMembers); 
        const memberDetails = await Promise.all(
          filteredMembers.map(async (member) => {
            const resp = await fetch(`/api/members/${member.memberID}/getUserIDByMemberID`); 
            const userData = await resp.json(); 
            const userID = userData.userID; 

            const userResp = await fetch(`/api/users/${userID}`); 
            const data = await userResp.json(); 

            return {
              ...member, 
              firstName: data.firstName, 
              lastName: data.lastName,
              userID: userID, 
            }; 
          })
        ); 
        setMembers(memberDetails);
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

    const newOwner = members.find(member => member.memberID === selectedUserId); 
    if (!newOwner) {
      setErrorMessage('Selected member user not found.'); 
      return; 
    }

    const newOwnerUserID = newOwner.userID;

    try {
      const response = await fetch(`/api/projects/${projectID}/transferOwnership?newOwnerUserID=${newOwnerUserID}`, {
        method: 'POST',
      });

      if (!response.ok) throw new Error('Transfer failed')


      // navigate(`/projects/${projectID}/ownership`);

      navigate('/'); 
    } catch (error) {
      setErrorMessage('Error transferring ownership.');
    }
  };

  return (
    <div className={styles['ownership']}>
      <h2>Transfer Ownership</h2>

      {loading ? (
        <p className={styles['centered-message']}>Loading members...</p>
      ) : (
        <>
          {members.length === 0 ? (
            <p className={styles['centered-message']}> No other members to transfer ownership to.</p>
          ) : (
            <div className={styles['input-wrapper']}>
              <p className={styles['centered-message']}> Choose a member to become the new owner for this project</p>
              {members.map(member => (
                <label key={member.memberID} style={{color: 'white' }} className={styles['member-option']}>
                  <input
                    type="radio"
                    name="transferTarget"
                    value={member.memberID}
                    checked={selectedUserId === member.memberID}
                    onChange={() => setSelectedUserId(member.memberID)}
                  />
                  {member.firstName} {member.lastName}
                </label>
              ))}
              {errorMessage && <p className={styles['error']}>{errorMessage}</p>}
              <div className={styles['submit-button-container']}>
                <button className={styles['submit-button']} onClick={handleSubmit}>
                  Confirm Transfer
                </button>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
}
