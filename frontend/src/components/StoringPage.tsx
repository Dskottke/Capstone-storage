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
    errorModal: boolean
    successModal: boolean
    errorMessage: string
    successMessage: string
    setErrorModal: (showAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
    amountValue: string
    storageBinNumber: string
    itemNumber: string
    setStorageBinNumber: (storageBinNumber: string) => void
    setItemNumber: (itemNumber: string) => void
    setAmountValue: (amount: string) => void
    handleInputAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleInputItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleInputStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
}

function StoringPage(props: storingPageProps) {
    const [data, setData] = useState<DrivingOrder[]>([])

    const fetchData = () => {
        axios.get("/api/driving-orders/?type=INPUT")
            .then(response => {
                return response.data
            })
            .catch(error => {
                console.error(error)
            })
            .then((data) => setData(data))

    }

    useEffect(() => {
        fetchData()
    }, [])

    return (
        <div className={"page-container"}>
            <TableHeadNav/>
            <StoringNavigation fetchData={fetchData}
                               setErrorModal={props.setErrorModal}
                               setErrorMessage={props.setErrorMessage} setSuccessMessage={props.setSuccessMessage}
                               setSuccessModal={props.setSuccessModal} amountValue={props.amountValue}
                               setAmountValue={props.setAmountValue}
                               handleInputAmount={props.handleInputAmount}
                               handleInputItemNumber={props.handleInputItemNumber}
                               handleInputStorageBinNumber={props.handleInputStorageBinNumber}
                               setStorageBinNumber={props.setStorageBinNumber}
                               storageLocationNumber={props.storageBinNumber} itemNumber={props.itemNumber}
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
                <StoringTable drivingOrders={data}/>
            </div>
        </div>
    );
}

export default StoringPage;