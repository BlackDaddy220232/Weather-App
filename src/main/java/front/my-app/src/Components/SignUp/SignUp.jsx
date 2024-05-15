import React, { useState } from 'react'
import './SignUp.css'
import { FaUser } from "react-icons/fa";
import { FaLock } from "react-icons/fa";
import { FaEarthAmericas } from "react-icons/fa6";
import {  Link,useNavigate } from "react-router-dom"
import axios from "axios";

export const SignUp = () => {
  
  const navigate = useNavigate();
  
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [country, setCountry] = useState('');

   const handleSubmit = async (event) => {
    event.preventDefault(); 

        const data = {
            username: username,
            password: password,
            country: country
        };
        
        console.log(data.username);
        console.log(data.password);
        console.log(data.country);

  
      axios
      .post("http://localhost:8080/auth/signup", data)
      .then((response) => {
          navigate("/login");
      })
      .catch((error) => {
          console.error("Error:", error);
      });
  };
  return (
    <div className='container'>
      <form onSubmit={handleSubmit}>
      <div className='header'>
        <div className='text'>Sign Up</div>
      </div>
        <div className='inputs'>
          <div className='input'>
            <FaUser className='iconLogin'/>
            <input type="username" placeholder='username' 
            value={username} 
            onChange={(event) => setUsername(event.target.value)} 
            required  />
          </div>
          <div className='input'>
          <FaLock className='iconLogin' />
            <input type="password" placeholder='password'
            value={password} 
            onChange={(event) => setPassword(event.target.value)} 
            required  />
          </div>
          <div className='input'>
          <FaEarthAmericas className='iconLogin'/>
          <label htmlFor="country"></label>
        <select required 
        value={country} 
        onChange={(event) => setCountry(event.target.value)} >
            <option value="" >Country</option> 
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
          </div>
        </div>
       
        <button type="submit" className='register'>Register</button>

        <div className="register-link">
          <p> Already have an account? <Link to={"/login"}> Login
          </Link> </p>
        </div>
      </form>
    </div>
  )
}


export default SignUp;