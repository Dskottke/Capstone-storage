import React, {ChangeEvent, useEffect, useState} from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import RetrievalNavigation from "./RetrievalNavigation";
import RetrievalTable from "./RetrievalTable";
import axios from "axios";
import {DrivingOrder} from "../model/DrivingOrder";
import "../css/TablePage.css"
import Alert from "@mui/material/Alert";

type retrievalPageProps = {
    errorModal: boolean
    successModal: boolean
    errorMessage: string
    successMessage: string
    amountValue: string
    storageLocationId: string
    itemNumber: string
    setStorageLocationId: (storageBinNumber: string) => void
    setItemNumber: (itemNumber: string) => void
    setAmountValue: (amount: string) => void
    handleOutputAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleOutputItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleOutputStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
    setErrorModal: (showAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function RetrievalPage(props: retrievalPageProps) {

    const [retrievalData, setRetrievalData] = useState<DrivingOrder[]>([])

    const fetchRetrievalData = () => {
        axios.get("/api/driving-orders/?type=OUTPUT")
            .then(response => {
                return response.data
            })
            .catch(error => {
                console.error(error)
            })
            .then((data) => setRetrievalData(data))
    }

    useEffect(() => {
        fetchRetrievalData()
    }, [])

    return (
        <div className={"page-container"}>
            <TableHeadNav/>
            <RetrievalNavigation
                setSuccessModal={props.setSuccessModal}
                setErrorModal={props.setErrorModal}
                setErrorMessage={props.setErrorMessage}
                setSuccessMessage={props.setSuccessMessage}
                fetchRetrievalData={fetchRetrievalData}
                amountValue={props.amountValue}
                setAmountValue={props.setAmountValue}
                handleOutputAmount={props.handleOutputAmount}
                handleOutputItemNumber={props.handleOutputItemNumber}
                handleOutputStorageBinNumber={props.handleOutputStorageBinNumber}
                setStorageLocationId={props.setStorageLocationId}
                storageLocationId={props.storageLocationId} itemNumber={props.itemNumber}
                setItemNumber={props.setItemNumber}/>

            {props.errorModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    props.setErrorModal(false);
                }}>{props.errorMessage}</Alert>
            }
            {props.successModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} onClose={() => {
                    props.setSuccessModal(false);
                }}>{props.successMessage}</Alert>
            }
            <div className={"page-body"}>

                <RetrievalTable fetchRetrievalData={fetchRetrievalData} setSuccessMessage={props.setSuccessMessage}
                                setSuccessModal={props.setSuccessModal}
                                setErrorModal={props.setErrorModal}
                                setErrorMessage={props.setErrorMessage}
                                drivingOutputOrders={retrievalData}/>
            </div>
        </div>
    );
}


export default RetrievalPage;