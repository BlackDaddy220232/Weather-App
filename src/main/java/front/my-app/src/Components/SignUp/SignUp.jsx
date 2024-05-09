import React, { useState } from 'react'
import './LoginSignUp.css'
import { FaUser } from "react-icons/fa";
import { FaLock } from "react-icons/fa";
import { FaEarthAmericas } from "react-icons/fa6";

export const LoginSignUp = () => {
  
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault(); 
    console.log("Form submitted");
    const formData = {
      username: username,
      password: password
    };

    try {
      const response = await fetch('http://localhost:8080/auth/signin', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      });

      if (response.ok) {
        console.log('Login successful');
      } else {

        console.error('Login failed');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };
  return (
    <div className='container'>
      <form action="">
      <div className='header'>
        <div className='text'>{action}</div>
      </div>
        <div className='inputs'>
          <div className='input'>
            <FaUser className='icon'/>
            <input type="username" placeholder='username' />
          </div>
          <div className='input'>
          <FaLock className='icon' />
            <input type="password" placeholder='password' />
          </div>
          {action==="Login"?<div></div>:<div className='input'>
          <FaEarthAmericas className='icon'/>
          <label htmlFor="country"></label>
        <select required>
            <option value="" disabled selected hidden>Country</option> 
            <option value="Argentina">Argentina</option>
            <option value="Australia">Australia</option>
            <option value="Austria">Austria</option>
            <option value="Belarus">Belarus</option>
            <option value="Brazil">Brazil</option>
            <option value="Canada">Canada</option>
            <option value="China">China</option>
            <option value="Denmark">Denmark</option>
            <option value="Egypt">Egypt</option>
            <option value="Finland">Finland</option>
            <option value="France">France</option>
            <option value="Germany">Germany</option>
            <option value="Greece">Greece</option>
            <option value="India">India</option>
            <option value="Italy">Italy</option>
            <option value="Japan">Japan</option>
            <option value="Mexico">Mexico</option>
            <option value="Netherlands">Netherlands</option>
            <option value="Nigeria">Nigeria</option>
            <option value="Norway">Norway</option>
            <option value="Poland">Poland</option>
            <option value="Portugal">Portugal</option>
            <option value="Russia">Russia</option>
            <option value="South Africa">South Africa</option>
            <option value="South Korea">South Korea</option>
            <option value="Spain">Spain</option>
            <option value="Sweden">Sweden</option>
            <option value="Switzerland">Switzerland</option>
            <option value="Turkey">Turkey</option>
            <option value="UK">United Kingdom</option>
            <option value="USA">United States of America</option>
            <option value="Ukraine">Ukraine</option>
        </select>
          </div>}
        </div>
        <div className="submit-container">
          <button type="submit">Login</button>
      </div>
      </form>
    </div>
  )
}