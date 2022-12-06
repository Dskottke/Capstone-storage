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
    retrievalErrorModal: boolean
    retrievalSuccessModal: boolean
    retrievalErrorMessage: string
    retrievalSuccessMessage: string
    retrievalAmountValue: string
    retrievalStorageLocationId: string
    retrievalItemNumber: string
    setRetrievalStorageLocationId: (storageBinNumber: string) => void
    setRetrievalItemNumber: (itemNumber: string) => void
    setRetrievalAmountValue: (amount: string) => void
    handleRetrievalAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleRetrievalItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleRetrievalStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
    setRetrievalErrorModal: (showAlert: boolean) => void
    setRetrievalErrorMessage: (errorMessage: string) => void
    setRetrievalSuccessMessage: (successMessage: string) => void
    setRetrievalSuccessModal: (showSuccessAlert: boolean) => void
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
                setRetrievalSuccessModal={props.setRetrievalSuccessModal}
                setRetrievalErrorModal={props.setRetrievalErrorModal}
                setRetrievalErrorMessage={props.setRetrievalErrorMessage}
                setRetrievalSuccessMessage={props.setRetrievalSuccessMessage}
                fetchRetrievalData={fetchRetrievalData}
                retrievalAmountValue={props.retrievalAmountValue}
                setRetrievalAmountValue={props.setRetrievalAmountValue}
                handleRetrievalAmount={props.handleRetrievalAmount}
                handleRetrievalItemNumber={props.handleRetrievalItemNumber}
                handleRetrievalStorageBinNumber={props.handleRetrievalStorageBinNumber}
                setRetrievalStorageLocationId={props.setRetrievalStorageLocationId}
                retrievalStorageLocationId={props.retrievalStorageLocationId}
                retrievalItemNumber={props.retrievalItemNumber}
                setRetrievalItemNumber={props.setRetrievalItemNumber}/>

            {props.retrievalErrorModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    props.setRetrievalErrorModal(false);
                }}>{props.retrievalErrorMessage}</Alert>
            }
            {props.retrievalSuccessModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} onClose={() => {
                    props.setRetrievalSuccessModal(false);
                }}>{props.retrievalSuccessMessage}</Alert>
            }
            <div className={"page-body"}>

                <RetrievalTable fetchRetrievalData={fetchRetrievalData}
                                setRetrievalSuccessMessage={props.setRetrievalSuccessMessage}
                                setRetrievalSuccessModal={props.setRetrievalSuccessModal}
                                setRetrievalErrorModal={props.setRetrievalErrorModal}
                                setRetrievalErrorMessage={props.setRetrievalErrorMessage}
                                drivingOutputOrders={retrievalData}/>
            </div>
        </div>
    );
}


export default RetrievalPage;