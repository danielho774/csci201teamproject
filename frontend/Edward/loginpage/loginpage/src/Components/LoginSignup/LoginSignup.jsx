import React from 'react'
import './LoginSignup.css'

import userIcon from '../Assets/person.png'
import emailIcon from '../Assets/email.png'
import passwordIcon from '../Assets/password.png'

export const LoginSignup = () => {

  const [action, setAction] = React.useState("Log In");
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
          <div className="input"> 
          <img src={userIcon} alt="" />
          <input type="text" placeholder = "Name"/>
          </div>}
        {/*This ternary operator makes it so that the Name field is hidden on the Login page. Because you don't need that*/}
        <div className="input">
          <img src={emailIcon} alt="" />
          <input type="email" placeholder = "Email"/>
        </div>
        <div className="input">
          <img src={passwordIcon} alt="" />
          <input type="password" placeholder = "Password"/>
        </div>
      </div>
      {action === "Sign Up" ? <div></div> : <div className="forgot-password">Forgot Your Password? <span>Click Here!</span></div>}
      {/*This ternary operator hides forgot password from the Sign Up page, because there is no password for you to forget */}
      <div className="submit-container">
        <div className={action === "Log In" ? "submit gray" : "submit"} onClick={() => {setAction("Sign Up")}}>Sign Up</div>
        <div className={action === "Sign Up" ? "submit gray" : "submit"} onClick={() => { setAction("Log In") }}>Log In</div>
        {/*These two blocks control which page you're in: Login or Sign Up, clicking on one page prevents you from re-clicking it again, and greys out the button*/}
      </div>
    </div> 
  )
}
