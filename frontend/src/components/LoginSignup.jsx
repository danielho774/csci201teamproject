import React from 'react'
import './LoginSignup.css'

import userIcon from '../Assets/person.png'
import emailIcon from '../Assets/email.png'
import passwordIcon from '../Assets/password.png'


export const LoginSignup = ({onLogin}) => {
  
  const [username, setUsername] = React.useState("");
  const [firstName, setFN] = React.useState("");
  const [lastName, setLN] = React.useState("");
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");

  const [action, setAction] = React.useState("Log In");


  async function processLogin({email, password }){
    try {
      const response = await fetch('http://localhost:8080/api/users/login', {
        method: 'POST', 
        headers: {'Content-Type': 'application/json'}, 
        body: JSON.stringify({email, password}), 
      }); 

      if (!response.ok) {
        throw new Error ('Login failed. ISSUE'); 
      }

      const data = await response.json(); 
      console.log('Logged in user: ', data); 
      onLogin(data); 
    }
    catch(error) {
      console.error('Login error: ', error); 
      alert('Login failed: ' + error.message); 
    }
  }

  async function processSignup({ username, firstName, lastName, email, password }){
    const result = await fetch('http://localhost:8080/api/users/register'', 
      {method: 'POST',
        headers: {'Content-Type':'application/json'},
        body: JSON.stringify({username, email, password, firstName, lastName}),
      });
      if(!result.ok){
        throw new Error("Error connecting to backend");
      }

      const data = await result.json();
      const userID = data.userId;
      console.log(userID);


      //localStorage.setItem('logged-in', 'true');
      //localStorage.setItem('user-email', );
      onLogin();
      //navigate('/');
  }
  //usestate is what React calls a Hook, it enables functional components to manage the state of the variable
  //also note that you can import the React Usestate instead of directly calling it like I did :P
  return (
    <div className='container'>
      <div className="header">
        <div className="text">{action}</div>
        <div className="underline"></div>
      </div> 
      <div className="inputs">
        {action === "Log In" ? <div></div> :
        <>
          <div className="input"> 
            <img src={userIcon} alt="" />
            <input type="text" placeholder = "Username" value = {username} onChange = {(e) => setUsername(e.target.value)}/>
          </div>
          <div className="input"> 
          <img src={userIcon} alt="" />
            <input type="text" placeholder = "First Name" value = {firstName} onChange = {(e) => setFN(e.target.value)}/>
          </div>
          <div className="input">
          <img src={userIcon} alt="" /> 
            <input type="text" placeholder = "Last Name" value = {lastName} onChange = {(e) => setLN(e.target.value)}/>
          </div>
          </>}
        {/*This ternary operator makes it so that the Name field is hidden on the Login page. Because you don't need that*/}
        <div className="input">
          <img src={emailIcon} alt="" />
          <input type="email" placeholder = "Email"value={email} onChange={(e) => setEmail(e.target.value)}/>
        </div>
        <div className="input">
          <img src={passwordIcon} alt="" />
          <input type="password" placeholder = "Password" value={password} onChange={(e) => setPassword(e.target.value)}/>
        </div>
      </div>
      {action === "Sign Up" ? <div></div> : <div className="forgot-password">Forgot Your Password? <span>Click Here!</span></div>}
      {/*This ternary operator hides forgot password from the Sign Up page, because there is no password for you to forget */}
      <div className="submit-container">
        <div className={action === "Log In" ? "submit gray" : "submit"} onClick={() => {setAction("Sign Up")}}>Sign Up</div>
        <div className={action === "Sign Up" ? "submit gray" : "submit"} onClick={() => { setAction("Log In") }}>Log In</div>
        {/*These two blocks control which page you're in: Login or Sign Up, clicking on one page prevents you from re-clicking it again, and greys out the button*/}
      </div>
      <div className="submit-button" onClick={() => {
        if (action === "Log In") {
          processLogin({email, password});
        } else {
          processSignup({ username, firstName, lastName, email, password });
        }
      }}>
        {action}
        </div>
    </div> 
  )
}
