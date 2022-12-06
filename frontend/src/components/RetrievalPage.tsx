import React, {ChangeEvent, useEffect, useState} from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import RetrievalNavigation from "./RetrievalNavigation";
import RetrievalTable from "./RetrievalTable";
import axios from "axios";
import {DrivingOrder} from "../model/DrivingOrder";
import "../css/TablePage.css"

type retrievalPageProps = {
    amountValue: string
    storageLocationId: string
    itemNumber: string
    setStorageLocationId: (storageBinNumber: string) => void
    setItemNumber: (itemNumber: string) => void
    setAmountValue: (amount: string) => void
    handleInputAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleInputItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleInputStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
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
            <RetrievalNavigation fetchRetrievalData={fetchRetrievalData} amountValue={props.amountValue}
                                 setAmountValue={props.setAmountValue}
                                 handleInputAmount={props.handleInputAmount}
                                 handleInputItemNumber={props.handleInputItemNumber}
                                 handleInputStorageBinNumber={props.handleInputStorageBinNumber}
                                 setStorageLocationId={props.setStorageLocationId}
                                 storageLocationId={props.storageLocationId} itemNumber={props.itemNumber}
                                 setItemNumber={props.setItemNumber}/>
            <div className={"page-body"}>
                <RetrievalTable drivingOrders={retrievalData}/>
            </div>
        </div>
    );
}


export default RetrievalPage;