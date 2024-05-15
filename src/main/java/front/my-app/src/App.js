// App.jsx
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
// import Header from "./components/header/Header";
import Login from "./Components/Login/Login";
import SignUp from "./Components/SignUp/SignUp";
import Home from "./Components/HomePage/HomePage";


class App extends React.Component {

  render() {
      return (
          <Router>
              <div className="App">
                  <Routes>
                      <Route path="/" element={<Login />} /> 
                      <Route path="/login" element={<Login/>} />
                      <Route path="/signup" element={<SignUp/>} /> 
                      <Route path="/home" element={<Home/>} /> 
                  </Routes>
              </div>
          </Router>
      );
  }
}

export default App;