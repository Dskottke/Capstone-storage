import React, {ChangeEvent, useEffect, useState} from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import StoringNavigation from "./StoringNavigation";
import StoringTable from "./StoringTable";
import {DrivingOrder} from "../model/DrivingOrder";
import axios from "axios";
import "../css/TablePage.css"
import Alert from "@mui/material/Alert";

type storingPageProps = {
    storingErrorModal: boolean
    storingSuccessModal: boolean
    storingErrorMessage: string
    storingSuccessMessage: string
    setStoringErrorModal: (showAlert: boolean) => void
    setStoringErrorMessage: (errorStoringMessage: string) => void
    setStoringSuccessMessage: (successStoringMessage: string) => void
    setStoringSuccessModal: (showStoringSuccessAlert: boolean) => void
    storingAmountValue: string
    storingStorageLocationId: string
    storingItemNumber: string
    setStoringStorageLocationId: (storingStorageBinNumber: string) => void
    setStoringItemNumber: (storingItemNumber: string) => void
    setStoringAmountValue: (storingAmount: string) => void
    handleStoringAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleStoringItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleStoringStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
}

function StoringPage(props: storingPageProps) {
    const [inputDrivingOrders, setInputDrivingOrders] = useState<DrivingOrder[]>([])

    const fetchInputOrders = () => {
        axios.get("/api/driving-orders/?type=INPUT")
            .then(response => {
                return response.data
            })
            .catch(error => {
                console.error(error)
            })
            .then((data) => setInputDrivingOrders(data))
    }

    useEffect(() => {
        fetchInputOrders()
    }, [])

    return (
        <div className={"page-container"}>
            <TableHeadNav/>
            <StoringNavigation fetchStoringData={fetchInputOrders}
                               setStoringErrorModal={props.setStoringErrorModal}
                               setStoringErrorMessage={props.setStoringErrorMessage}
                               setStoringSuccessMessage={props.setStoringSuccessMessage}
                               setStoringSuccessModal={props.setStoringSuccessModal}
                               storingAmountValue={props.storingAmountValue}
                               setStoringAmountValue={props.setStoringAmountValue}
                               handleStoringAmount={props.handleStoringAmount}
                               handleStoringItemNumber={props.handleStoringItemNumber}
                               handleStoringStorageBinNumber={props.handleStoringStorageBinNumber}
                               setStoringStorageBinNumber={props.setStoringStorageLocationId}
                               storingStorageLocationId={props.storingStorageLocationId}
                               storingItemNumber={props.storingItemNumber}
                               setStoringItemNumber={props.setStoringItemNumber}/>

            {props.storingErrorModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    props.setStoringErrorModal(false);
                }}>{props.storingErrorMessage}</Alert>
            }
            {props.storingSuccessModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} onClose={() => {
                    props.setStoringSuccessModal(false);
                }}>{props.storingSuccessMessage}</Alert>
            }
            <div className={"page-body"}>
                <StoringTable fetchData={fetchInputOrders} setStoringSuccessMessage={props.setStoringSuccessMessage}
                              setStoringSuccessModal={props.setStoringSuccessModal}
                              setStoringErrorModal={props.setStoringErrorModal}
                              setStoringErrorMessage={props.setStoringErrorMessage}
                              storingDrivingOrders={inputDrivingOrders}/>
            </div>
        </div>
    );
}

export default StoringPage;