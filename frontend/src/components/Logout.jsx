
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function Logout({setIsLoggedIn}) {
  const navigate = useNavigate();

  useEffect(() => {
    localStorage.removeItem('userID');
    localStorage.setItem('logged-in', 'false');
    setIsLoggedIn(false);
    navigate('/login');
  }, [navigate]);

  return null;
}

export default Logout;
