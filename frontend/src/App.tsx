import React, {ChangeEvent, useState} from 'react';
import ItemPage from "./components/ItemPage";
import './App.css';
import MainPage from "./components/MainPage";
import {Route, Routes} from "react-router-dom";
import StoragePage from "./components/StoragePage";
import StoringPage from "./components/StoringPage";
import RetrievalPage from "./components/RetrievalPage";


function App() {

    const [storageBinNumber, setStorageBinNumber] = useState("")
    const [itemNumber, setItemNumber] = useState("")
    const [amountValue, setAmountValue] = useState("")

    const handleInputAmount = (event: ChangeEvent<HTMLInputElement>) => {
        const validAmount = event.target.value.replace(/\D/g, '')
        setAmountValue(validAmount)
    }
    const handleInputItemNumber = (event: ChangeEvent<HTMLInputElement>) => {
        const validItemNumber = event.target.value.replace(/\D/g, '')
        setItemNumber(validItemNumber)
    }
    const handleInputStorageBinNumber = (event: ChangeEvent<HTMLInputElement>) => {
        const validStorageBinNumber = event.target.value.replace(/\D/g, '')
        setStorageBinNumber(validStorageBinNumber)
    }
    return (<>
            <Routes>
                <Route path={"/"} element={<MainPage/>}/>
                <Route path={"/item-page"} element={<ItemPage/>}/>
                <Route path={"/storage-page"} element={<StoragePage/>}/>
                <Route path={"/store-page"}
                       element={<StoringPage amountValue={amountValue} setAmountValue={setAmountValue}
                                             handleInputAmount={handleInputAmount}
                                             handleInputItemNumber={handleInputItemNumber}
                                             handleInputStorageBinNumber={handleInputStorageBinNumber}
                                             setStorageBinNumber={setStorageBinNumber}
                                             storageBinNumber={storageBinNumber} itemNumber={itemNumber}
                                             setItemNumber={setItemNumber}/>}/>
                <Route path={"/retrieval-page"}
                       element={<RetrievalPage amountValue={amountValue} setAmountValue={setAmountValue}
                                               handleInputAmount={handleInputAmount}
                                               handleInputItemNumber={handleInputItemNumber}
                                               handleInputStorageBinNumber={handleInputStorageBinNumber}
                                               setStorageBinNumber={setStorageBinNumber}
                                               storageBinNumber={storageBinNumber} itemNumber={itemNumber}
                                               setItemNumber={setItemNumber}/>}/>
            </Routes>

        </>
    );
}

export default App;
