import React, {useEffect, useState} from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import StoringNavigation from "./StoringNavigation";
import StoringTable from "./StoringTable";
import {DrivingOrder} from "../model/DrivingOrder";
import axios from "axios";
import "../css/TablePage.css"

function StoringPage() {
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
            <StoringNavigation/>
            <div className={"page-body"}>
                <StoringTable drivingOrders={data}/>
            </div>
        </div>
    );
}

export default StoringPage;