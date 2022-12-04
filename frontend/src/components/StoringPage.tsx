import React, {ChangeEvent, useEffect, useState} from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import StoringNavigation from "./StoringNavigation";
import StoringTable from "./StoringTable";
import {DrivingOrder} from "../model/DrivingOrder";
import axios from "axios";
import "../css/TablePage.css"

type storingPageProps = {
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
            <StoringNavigation amountValue={props.amountValue} setAmountValue={props.setAmountValue}
                               handleInputAmount={props.handleInputAmount}
                               handleInputItemNumber={props.handleInputItemNumber}
                               handleInputStorageBinNumber={props.handleInputStorageBinNumber}
                               setStorageBinNumber={props.setStorageBinNumber}
                               storageBinNumber={props.storageBinNumber} itemNumber={props.itemNumber}
                               setItemNumber={props.setItemNumber}/>
            <div className={"page-body"}>
                <StoringTable drivingOrders={data}/>
            </div>
        </div>
    );
}

export default StoringPage;