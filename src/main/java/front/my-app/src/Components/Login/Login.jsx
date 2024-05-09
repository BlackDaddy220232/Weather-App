import React, { useState } from 'react';
import './Login.css';
import { FaUser, FaLock } from "react-icons/fa";
import { FaEarthAmericas } from "react-icons/fa6";

export const Login = () => {
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
        const jwtToken = await response.text();
        document.cookie = `token=${jwtToken}; path=/`;
        console.log('Login successful');
        console.log('Token:', jwtToken);

        // Выполните другие действия с использованием токена

        // Отправка запроса на http://localhost:8080/users/getAllCities
        const citiesResponse = await fetch('http://localhost:8080/users/getAllCities', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${jwtToken}`
          }
        });

        if (citiesResponse.ok) {
          const citiesData = await citiesResponse.json();
          console.log('Cities:', citiesData);
          // Обработайте полученные данные городов
        } else {
          console.error('Failed to fetch cities');
        }
      } else {
        console.log(username);
        console.log(password);
        console.error('Login failed');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <div className='container'>
      <form onSubmit={handleSubmit}>
        <div className='header'>
          <div className='text'>Login</div>
        </div>
        <div className='inputs'>
          <div className='input'>
            <FaUser className='icon'/>
            <input type="username" placeholder='username' value={username} onChange={(event) => setUsername(event.target.value)} />
          </div>
          <div className='input'>
            <FaLock className='icon' />
            <input type="password" placeholder='password' value={password} onChange={(event) => setPassword(event.target.value)} />
          </div>
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  );
};