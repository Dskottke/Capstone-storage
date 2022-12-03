import React, {useEffect, useState} from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import RetrievalNavigation from "./RetrievalNavigation";
import RetrievalTable from "./RetrievalTable";
import axios from "axios";
import {DrivingOrder} from "../model/DrivingOrder";
import "../css/TablePage.css"

function RetrievalPage() {

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
            <RetrievalNavigation/>
            <div className={"page-body"}>
                <RetrievalTable drivingOrders={data}/>
            </div>
        </div>
    );
}


export default RetrievalPage;