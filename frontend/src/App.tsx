import React from 'react';
import ItemPage from "./components/ItemPage";
import './App.css';
import MainPage from "./components/MainPage";
import {Route, Routes} from "react-router-dom";
import StoragePage from "./components/StoragePage";
import StoringPage from "./components/StoringPage";
import DischargingPage from "./components/DischargingPage";


function App() {


    return (<>
            <Routes>
                <Route path={"/"} element={<MainPage/>}/>
                <Route path={"/item-page"} element={<ItemPage/>}/>
                <Route path={"/storage-page"} element={<StoragePage/>}/>
                <Route path={"/store-page"} element={<StoringPage/>}/>
                <Route path={"/discharging-page"} element={<DischargingPage/>}/>
            </Routes>

        </>
  );
}

export default App;
