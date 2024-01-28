import logo from './logo.svg';
import './App.css';

import React, { useState, useEffect } from 'react';
import axios from 'axios';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>

		<button onClick={()=>{getData();}}>SpringBoot Call to console log</button>
      </header>
    </div>
  );
}

async function getData() {
	try {
	  //응답 성공
	  const response = await axios.get('http://localhost:8080');
	  console.log(response.data);
	} catch (error) {
	  //응답 실패
	  console.error(error);
	}
  }

export default App;
