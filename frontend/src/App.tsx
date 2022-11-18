import React, {useEffect, useState} from 'react';

import './App.css';
import axios from "axios";





function App() {

const [data,setData] = useState()

    const test = () => {
        axios.get("/test")
            .then(response => response.data)
            .then(data => setData(data))
    }

    useEffect(test)
    return (<>

            {data}






</>
  );
}

export default App;
