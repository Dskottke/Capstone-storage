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
    storingPageErrorModal: boolean
    storingPageSuccessModal: boolean
    storingPageErrorMessage: string
    storingPageSuccessMessage: string
    setStoringPageErrorModal: (showAlert: boolean) => void
    setStoringPageErrorMessage: (errorStoringMessage: string) => void
    setStoringPageSuccessMessage: (successStoringMessage: string) => void
    setStoringPageSuccessModal: (showStoringSuccessAlert: boolean) => void
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
                               setStoringNavigationErrorModal={props.setStoringPageErrorModal}
                               setStoringNavigationErrorMessage={props.setStoringPageErrorMessage}
                               setStoringNavigationSuccessMessage={props.setStoringPageSuccessMessage}
                               setStoringNavigationSuccessModal={props.setStoringPageSuccessModal}
                               storingAmountValue={props.storingAmountValue}
                               setStoringAmountValue={props.setStoringAmountValue}
                               handleStoringAmount={props.handleStoringAmount}
                               handleStoringItemNumber={props.handleStoringItemNumber}
                               handleStoringStorageBinNumber={props.handleStoringStorageBinNumber}
                               setStoringStorageBinNumber={props.setStoringStorageLocationId}
                               storingStorageLocationId={props.storingStorageLocationId}
                               storingItemNumber={props.storingItemNumber}
                               setStoringItemNumber={props.setStoringItemNumber}/>

            {props.storingPageErrorModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    props.setStoringPageErrorModal(false);
                }}>{props.storingPageErrorMessage}</Alert>
            }
            {props.storingPageSuccessModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} onClose={() => {
                    props.setStoringPageSuccessModal(false);
                }}>{props.storingPageSuccessMessage}</Alert>
            }
            <div className={"page-body"}>
                <StoringTable fetchData={fetchInputOrders}
                              setStoringTableSuccessMessage={props.setStoringPageSuccessMessage}
                              setStoringTableSuccessModal={props.setStoringPageSuccessModal}
                              setStoringTableErrorModal={props.setStoringPageErrorModal}
                              setStoringTableErrorMessage={props.setStoringPageErrorMessage}
                              storingDrivingOrders={inputDrivingOrders}/>
            </div>
        </div>
    );
}

export default StoringPage;