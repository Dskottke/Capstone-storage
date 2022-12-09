import React, {ChangeEvent, useState} from 'react';
import ItemPage from "./components/ItemPage";
import './App.css';
import MainPage from "./components/MainPage";
import {Route, Routes} from "react-router-dom";
import StoragePage from "./components/StoragePage";
import StoringPage from "./components/StoringPage";
import RetrievalPage from "./components/RetrievalPage";


function App() {

    const [storageLocationId, setStorageLocationId] = useState("")
    const [itemNumber, setItemNumber] = useState("")
    const [amountValue, setAmountValue] = useState("")
    const [errorModal, setErrorModal] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")
    const [successModal, setSuccessModal] = useState(false)
    const [successMessage, setSuccessMessage] = useState("")



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
        setStorageLocationId(validStorageBinNumber)
    }
    return  (<>
            <Routes>
                <Route path={"/"} element={<MainPage/>}/>
                <Route path={"/item-page"}
                       element={<ItemPage itemSuccessModal={successModal} itemSuccessMessage={successMessage}
                                          itemErrorMessage={errorMessage} itemErrorModal={errorModal}
                                          setItemErrorModal={setErrorModal}
                                          setItemErrorMessage={setErrorMessage}
                                          setItemSuccessMessage={setSuccessMessage}
                                          setItemSuccessModal={setSuccessModal}/>}/>
                <Route path={"/storage-page"} element={<StoragePage/>}/>
                <Route path={"/store-page"}
                       element={<StoringPage storingPageErrorModal={errorModal} storingPageErrorMessage={errorMessage}
                                             setStoringPageSuccessModal={setSuccessModal}
                                             setStoringPageSuccessMessage={setSuccessMessage}
                                             setStoringPageErrorMessage={setErrorMessage}
                                             setStoringPageErrorModal={setErrorModal}
                                             storingPageSuccessMessage={successMessage}
                                             storingPageSuccessModal={successModal} storingAmountValue={amountValue}
                                             setStoringAmountValue={setAmountValue}
                                             handleStoringAmount={handleInputAmount}
                                             handleStoringItemNumber={handleInputItemNumber}
                                             handleStoringStorageBinNumber={handleInputStorageBinNumber}
                                             setStoringStorageLocationId={setStorageLocationId}
                                             storingStorageLocationId={storageLocationId}
                                             storingItemNumber={itemNumber}
                                             setStoringItemNumber={setItemNumber}/>}/>
                <Route path={"/retrieval-page"}
                       element={<RetrievalPage retrievalPageErrorModal={errorModal}
                                               retrievalPageErrorMessage={errorMessage}
                                               setRetrievalPageSuccessModal={setSuccessModal}
                                               setRetrievalPageSuccessMessage={setSuccessMessage}
                                               setRetrievalPageErrorMessage={setErrorMessage}
                                               setRetrievalPageErrorModal={setErrorModal}
                                               retrievalPageSuccessMessage={successMessage}
                                               retrievalPageSuccessModal={successModal}
                                               retrievalAmountValue={amountValue}
                                               setRetrievalAmountValue={setAmountValue}
                                               handleRetrievalAmount={handleInputAmount}
                                               handleRetrievalItemNumber={handleInputItemNumber}
                                               handleRetrievalStorageBinNumber={handleInputStorageBinNumber}
                                               setRetrievalStorageLocationId={setStorageLocationId}
                                               retrievalStorageLocationId={storageLocationId}
                                               retrievalItemNumber={itemNumber}
                                               setRetrievalItemNumber={setItemNumber}/>}/>
            </Routes>

        </>
    );
}

export default App;
