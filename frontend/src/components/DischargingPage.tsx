import React, {useEffect, useState} from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import DischargingNavigation from "./DischargingNavigation";
import DischargingTable from "./DischargingTable";
import axios from "axios";
import {DrivingOrder} from "../model/DrivingOrder";
import "../css/TablePage.css"

function DischargingPage() {

    const [data, setData] = useState<DrivingOrder[]>([])

    const fetchData = () => {
        axios.get("/api/driving-orders/?type=OUTPUT")
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
            <DischargingNavigation/>
            <div className={"page-body"}>
                <DischargingTable drivingOrders={data}/>
            </div>
        </div>
    );
}


export default DischargingPage;