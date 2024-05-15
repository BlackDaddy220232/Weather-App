import Cookies from 'js-cookie';
import React, { useState,useContext, useRef,useEffect  } from 'react';
import {  Link,useNavigate } from "react-router-dom"
import './HomePage.css';
import axios from "axios";
import { BsFillGeoAltFill } from "react-icons/bs";
import { IoWaterSharp } from "react-icons/io5";
import { WiSunset } from "react-icons/wi";
import { WiSunrise } from "react-icons/wi";
import { FiWind } from "react-icons/fi";
import { MdVisibility } from "react-icons/md";
import { MdOutlineSpeed } from "react-icons/md";
import { IoHeartDislike } from "react-icons/io5";
import { FaRegHeart } from "react-icons/fa";

export const HomePage = () => {

    const navigate = useNavigate();
    const [city, setCity] = useState('');
    const [tokenState,setTokenState] = useState('');
    const [isPlayerInList, setIsPlayerInList] = useState(false);
    const weatherDescription=Cookies.get('weatherDescription');
    const icon=Cookies.get('icon');
    const windSpeed=Cookies.get('windSpeed');
    const windDegrees=Cookies.get('windDegrees');
    const temperature=Cookies.get('temperature');
    const feelsLike=Cookies.get('feelsLike');
    const pressure=Cookies.get('pressure');
    const humidity=Cookies.get('humidity');
    const visibility=Cookies.get('visibility');
    const timezone=Cookies.get('timezone');
    const convertedSunsetTime=Cookies.get('convertedSunsetTime');
    const convertedSunriseTime=Cookies.get('convertedSunriseTime');
  const currentDate = new Date();
  const options = { weekday: 'long', month: 'short', day: 'numeric' };
  const formattedDate = currentDate.toLocaleDateString('en-US', options);
  const savedCitiesString = Cookies.get('savedCities');
  const cityname=Cookies.get('cityname');
  const cities = cityname ? JSON.parse(savedCitiesString) : [];
  const recentCitiesData = [];
  async function getWeatherDataForCity(cityname){
    const data = {
        city:cityname
    };
    console.log("Hello %s",cityname);
    axios
    .get("http://localhost:8080/api/v1/weather",{
        params: {
            cityname: data.city
        },
        headers: {
            Authorization: `Bearer ${tokenState}`
        }
    } )
    .then((response) => {
        const weatherData = response.data;
                const temperatureNew=weatherData.main.temp;
                const iconNew= weatherData.weather[0].icon;
                Cookies.set(`temperature-${cityname}`,temperatureNew);
                Cookies.set(`icon-${cityname}`,iconNew);
    })
    .catch((error) => {
        console.error("haha:", error);
    });
  }
  for (let i = 0; i < cities.length; i++) {
    const cityName = cities[i];
    console.log("YEEES");
    getWeatherDataForCity(cityName);
    const temperature = Cookies.get(`temperature-${cityName}`);
    const icon = Cookies.get(`icon-${cityName}`);
    recentCitiesData.push({ cityName, temperature, icon });
  }
    const WeatherIconMap = {
        "01d": "/Assets/WeatherIcon/clear-day.svg",
        "01n": "/Assets/WeatherIcon/clear-night.svg",
        "02d": "/Assets/WeatherIcon/partly-cloudy-day.svg",
        "02n": "/Assets/WeatherIcon/partly-cloudy-night.svg",
        "03d": "/Assets/WeatherIcon/cloudy.svg",
        "03n": "/Assets/WeatherIcon/cloudy.svg",
        "04d": "/Assets/WeatherIcon/overcast.svg",
        "04n": "/Assets/WeatherIcon/overcast.svg",
        "09d": "/Assets/WeatherIcon/heavy-showers.svg",
        "09n": "/Assets/WeatherIcon/heavy-showers.svg",
        "10d": "/Assets/WeatherIcon/showers.svg",
        "10n": "/Assets/WeatherIcon/showers.svg",
        "11d": "/Assets/WeatherIcon/thunderstorm-showers.svg",
        "11n": "/Assets/WeatherIcon/thunderstorm-showers.svg",
        "13d": "/Assets/WeatherIcon/snow.svg",
        "13n": "/Assets/WeatherIcon/snow.svg",
        "50d": "/Assets/WeatherIcon/fog.svg",
        "50n": "/Assets/WeatherIcon/fog.svg"
      };
     const weatherIcon = WeatherIconMap[icon];
  
     
     
     
     
     useEffect(() => {
        const token = Cookies.get('token');
        if (!token) {
          navigate('/message');
        } else {
          setTokenState(token);
        }
      }, [navigate, setTokenState]);

      const handleSubmit = async (event,city) => {
        event.preventDefault(); 
        const data = {
            city:city
        };
    
     
      axios
        .get("http://localhost:8080/api/v1/weather",{

            params: {
                cityname: data.city
            },
            headers: {
                Authorization: `Bearer ${tokenState}`
            }
        } )
        .then((response) => {
            const weatherData = response.data;
                    const citynameNew=data.city;
                    const weatherDescriptionNew= weatherData.weather[0].main;
                    const iconNew= weatherData.weather[0].icon;
                    const windSpeedNew = weatherData.wind.speed;
                    const windDegreesNew = weatherData.wind.deg;
                    const temperatureNew = weatherData.main.temp;
                    const feelsLikeNew = weatherData.main.feels_like;
                    const pressureNew = weatherData.main.pressure;
                    const humidityNew = weatherData.main.humidity;
                    const visibilityNew = weatherData.visibility;
                    const timezoneNew = weatherData.timezone;
                    const convertedSunsetTimeNew = weatherData.sys.convertedSunsetTime;
                    const convertedSunriseTimeNew = weatherData.sys.convertedSunriseTime;

                     Cookies.set('weatherDescription', weatherDescriptionNew);
                     Cookies.set('icon', iconNew);
                     Cookies.set('windSpeed', windSpeedNew);

                     Cookies.set('windDegrees', windDegreesNew);
                     Cookies.set('temperature', temperatureNew);
                     Cookies.set('feelsLike', feelsLikeNew);
                     Cookies.set('pressure', pressureNew);

                     Cookies.set('humidity', humidityNew);
                     Cookies.set('visibility', visibilityNew);
                     Cookies.set('timezone', timezoneNew);
                     Cookies.set('convertedSunsetTime', convertedSunsetTimeNew);
                     Cookies.set('convertedSunriseTime', convertedSunriseTimeNew);
                     Cookies.set('cityname',citynameNew);
                     for (let i = 0; i < Math.min(5, cities.length); i++) {
                        const cityName = cities[i];
                        console.log("YEEES");
                        getWeatherDataForCity(cityName);
                        const temperature = Cookies.get(`temperature-${cityName}`);
                        const icon = Cookies.get(`icon-${cityName}`);
                        recentCitiesData.push({ cityName, temperature, icon });
                      }
                     window.location.reload();
        })
        .catch((error) => {
            console.error("haha:", error);
        });
        
    };
    const handleDislike = async (event,city) => {
        console.log("sosi222");
        event.preventDefault(); 
        const data = {
            cityname: Cookies.get('cityname')
        };
    
     
      axios
        .delete("http://localhost:8080/users/deleteCity",{
            params:{
                city:cityname
            },
            headers: {
                Authorization: `Bearer ${tokenState}`
            }
        } )
        .then((response) => {
            console.log("sosi111");
          const  citiesString = Cookies.get('savedCities');
          console.log(citiesString);
  
   
          const cities = citiesString ? JSON.parse(citiesString) : [];  
          console.log(cities);
          console.log("City %s",data.city);
          const excludedCity = data.cityname;  
  
          const filteredCities = cities.filter(city => city !== excludedCity);
  
          console.log(excludedCity);
          
          console.log(filteredCities);
          const updatedCitiesString = JSON.stringify(filteredCities);
          console.log(savedCitiesString);
          
  
          Cookies.set('savedCities',updatedCitiesString);
          window.location.reload();
        })
        .catch((error) => {
            console.error("haha:", error);
        });
     
    };
    const handleLike = async (event) => {
        event.preventDefault(); 
        const data = {
            cityname: Cookies.get('cityname')
        };
        console.log(data.cityname);
    
        axios.post("http://localhost:8080/users/addCity?city=" + data.cityname, null, {
          headers: {
              Authorization: `Bearer ${tokenState}`
          }
      })
        .then((response) => {
          const citynameString = Cookies.get('savedCities');
          const cities = citynameString ? JSON.parse(citynameString) : [];  
          const citytoAdd = data.cityname; 
          if (!cities.includes(citytoAdd)) {
            cities.push(citytoAdd);
            const updatedNicknamesString = JSON.stringify(cities);
            Cookies.set('savedCities', updatedNicknamesString);
      
            console.log(citynameString);
            window.location.reload();
          }
        })
        .catch((error) => {
          console.error("haha:", tokenState);
    
            console.error("haha:", error);
        });
        
     
    };


  return (
    <div className='home-page-container'>
        <div className='left-container'>
            <form  onSubmit={(event) => handleSubmit(event, city)} >
            <div className='input-cities'>
            <input className='inputCity'
                         type="text" 
                         placeholder='Search for places' 
                         value={city} 
                         onChange={(event) => setCity(event.target.value)}  required
                        />
            </div>
            {cities.includes(cityname)? <button type="button" className='like' onClick={(event) => handleDislike(event)}>
             <IoHeartDislike className='likeIcon'/>
            </button>: <button type="button" className='like' onClick={(event) => handleLike(event)}>
             <FaRegHeart className='likeIcon'/>
            </button>}
            <div className='like'></div>
            <img className='weather-icon' src={weatherIcon} alt="Weather Icon"/>
            <div className='weather-temperature'>{Math.floor(temperature)}°</div>
            <div className='weather-description' >{weatherDescription}</div>
            <div className='data'>{formattedDate}</div>
            <div className='geolocation'><div className='geo-icon'><BsFillGeoAltFill /></div>{cityname}</div>
            </form>
        </div>
        <div className='right-container'>
             <div class="top-row">
             <ul className="list">
                {recentCitiesData.map((cityData, index) => (
                <button type="botton" key={index} className="blockCities"
                onClick={(event) => handleSubmit(event, cityData.cityName)} >
                    <div className='little-title'>{cityData.cityName}</div>
                    <img src={WeatherIconMap[cityData.icon]} className='little-icon'></img> 
                    <div className='little-temperature'>{Math.floor(cityData.temperature)}°</div>
                </button>
                ))}
            </ul>
            </div> 
            <div class="bottom-row">
                <div className='first-layer'>
                <div class="block">
                    <div class="icon-title-wrapper">
                    <div className="icon">
                     <FiWind />
                    </div>
                    <div className="title">Wind</div>
                    </div>
                    <div className="sys">{windSpeed} m/s</div>
                </div>
                    <div class="block">
                    <div class="icon-title-wrapper">
                    <div className="icon">
                     <IoWaterSharp />
                    </div>
                    <div className="title">Humidity</div>
                    </div>
                    <div className="sys">{humidity}%</div>
                </div>
                <div class="block">
                    <div class="icon-title-wrapper">
                    <div className="icon">
                     <MdVisibility />
                    </div>
                    <div className="title">Visibility</div>
                    </div>
                    <div className="sys">{visibility} m</div>
                </div>
                </div>
                <div className='second-layer'> 
                <div class="block">
                    <div class="icon-title-wrapper">
                    <div className="icon">
                     <MdOutlineSpeed />
                    </div>
                    <div className="title">Pressure</div>
                    </div>
                    <div className="sys">{pressure} hPA</div>
                </div>
                <div class="block">
                    <div class="icon-title-wrapper">
                    <div className="icon">
                     <WiSunrise />
                    </div>
                    <div className="title">Sunrise</div>
                    </div>
                    <div className="sys">{convertedSunriseTime}</div>
                </div>
                <div class="block">
                    <div class="icon-title-wrapper">
                    <div className="icon">
                     <WiSunset />
                    </div>
                    <div className="title">Sunset</div>
                    </div>
                    <div className="sys">{convertedSunsetTime}</div>
                </div>
                </div>
            </div>
        </div>
    </div>
  )
}

export default HomePage;
