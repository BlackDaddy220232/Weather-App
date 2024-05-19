import React, { useState } from 'react';
import './Login.css';
import { FaUser, FaLock } from "react-icons/fa";
import { FaEarthAmericas } from "react-icons/fa6";
import Cookies from 'js-cookie';
import axios from "axios";
import { Link, useNavigate } from "react-router-dom"
const getUser = "http://localhost:8080/users/getUserByUsername";

export const Login = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [firstCity, setFirstCity] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    console.log("Form submitted");

    const data = {
      username: username,
      password: password
    };
    axios
      .post("http://localhost:8080/auth/signin", data)
      .then((response) => {
        const token = response.data;
        console.log(token);
        localStorage.setItem('token', token);

        axios
          .get(getUser, {
            params: {
              username: data.username
            },
            headers: {
              Authorization: `Bearer ${token}`
            }
          })
          .then((response) => {
            const cities1 = response.data.savedCities.map(cities => cities.cityName);
            console.log(cities1);
            const citiesString = JSON.stringify(cities1);
            Cookies.set('token', token);
            Cookies.set('id', response.data.id);
            Cookies.set('username', response.data.username);
            Cookies.set('savedCities', citiesString);
            const savedCities = Cookies.get('savedCities');
            const cities = JSON.parse(savedCities);
            if (cities.length > 0) {
              setFirstCity(cities[0]);
            } else {
              setFirstCity("Moscow");
            }
            console.log(firstCity);
            // Отправка запроса на сервер
            axios.get(`http://localhost:8080/api/v1/weather?cityname=${firstCity}`)
              .then(response => {

                const weatherData = response.data;
                const weatherDescription = weatherData.weather[0].main;
                const icon = weatherData.weather[0].icon;
                const windSpeed = weatherData.wind.speed;
                const windDegrees = weatherData.wind.deg;
                const temperature = weatherData.main.temp;
                const feelsLike = weatherData.main.feels_like;
                const pressure = weatherData.main.pressure;
                const humidity = weatherData.main.humidity;
                const visibility = weatherData.visibility;
                const timezone = weatherData.timezone;
                const convertedSunsetTime = weatherData.sys.convertedSunsetTime;
                const convertedSunriseTime = weatherData.sys.convertedSunriseTime;
                const cityname = firstCity;

                Cookies.set('weatherDescription', weatherDescription);
                Cookies.set('icon', icon);
                Cookies.set('windSpeed', windSpeed);

                Cookies.set('windDegrees', windDegrees);
                Cookies.set('temperature', temperature);
                Cookies.set('feelsLike', feelsLike);
                Cookies.set('pressure', pressure);

                Cookies.set('humidity', humidity);
                Cookies.set('visibility', visibility);
                Cookies.set('timezone', timezone);
                Cookies.set('convertedSunsetTime', convertedSunsetTime);
                Cookies.set('convertedSunriseTime', convertedSunriseTime);
                Cookies.set('cityname', cityname);

                navigate("/home");
                window.location.reload();
              })
              .catch(error => {
                console.log('Ошибка при получении данных о погоде:', error);
              });

          })
      })
      .catch((error) => {
        console.error("Error:", error);
      });

  };

  return (
    <div className='container'>
      <form onSubmit={handleSubmit}>
        <div className='header'>
          <div className='text'>Login</div>
        </div>
        <div className='inputs'>
          <div className='input'>
            <FaUser className='iconLogin' />
            <input type="username" placeholder='username' value={username} onChange={(event) => setUsername(event.target.value)} />
          </div>
          <div className='input'>
            <FaLock className='iconLogin' />
            <input type="password" placeholder='password' value={password} onChange={(event) => setPassword(event.target.value)} />
          </div>
        </div>
        <button type="submit">Login</button>
      </form>
      <div className="register-link">
        <p> Don't have an account?  <Link to={"/signup"}> Register
        </Link></p>
      </div>
    </div>
  );
};

export default Login;