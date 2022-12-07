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
    retrievalPageErrorModal: boolean
    retrievalPageSuccessModal: boolean
    retrievalPageErrorMessage: string
    retrievalPageSuccessMessage: string
    retrievalAmountValue: string
    retrievalStorageLocationId: string
    retrievalItemNumber: string
    setRetrievalStorageLocationId: (retrievalStorageBinNumber: string) => void
    setRetrievalItemNumber: (retrievalItemNumber: string) => void
    setRetrievalAmountValue: (retrievalAmount: string) => void
    handleRetrievalAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleRetrievalItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleRetrievalStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
    setRetrievalPageErrorModal: (showRetrievalAlert: boolean) => void
    setRetrievalPageErrorMessage: (retrievalErrorMessage: string) => void
    setRetrievalPageSuccessMessage: (retrievalSuccessMessage: string) => void
    setRetrievalPageSuccessModal: (showRetrievalSuccessAlert: boolean) => void
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
                setRetrievalNavigationSuccessModal={props.setRetrievalPageSuccessModal}
                setRetrievalNavigationErrorModal={props.setRetrievalPageErrorModal}
                setRetrievalNavigationErrorMessage={props.setRetrievalPageErrorMessage}
                setRetrievalNavigationSuccessMessage={props.setRetrievalPageSuccessMessage}
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

            {props.retrievalPageErrorModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    props.setRetrievalPageErrorModal(false);
                }}>{props.retrievalPageErrorMessage}</Alert>
            }
            {props.retrievalPageSuccessModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} onClose={() => {
                    props.setRetrievalPageSuccessModal(false);
                }}>{props.retrievalPageSuccessMessage}</Alert>
            }
            <div className={"page-body"}>

                <RetrievalTable fetchRetrievalData={fetchRetrievalData}
                                setRetrievalTableSuccessMessage={props.setRetrievalPageSuccessMessage}
                                setRetrievalTableSuccessModal={props.setRetrievalPageSuccessModal}
                                setRetrievalTableErrorModal={props.setRetrievalPageErrorModal}
                                setRetrievalTableErrorMessage={props.setRetrievalPageErrorMessage}
                                drivingOutputOrders={retrievalData}/>
            </div>
        </div>
    );
}


export default RetrievalPage;
