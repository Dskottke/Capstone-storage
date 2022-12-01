import React, {useEffect, useState} from 'react';
import axios from "axios";
import {StorageBinModel} from "../model/StorageBinModel";
import "../css/TablePage.css"
import StorageTable from "./StorageTable";
import StorageNavigation from "./StorageNavigation";
import TableHeadNav from "./TableHeadNav";

function StoragePage() {

    const [data, setData] = useState<StorageBinModel[]>([]);

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axios.get("/api/storagebins/")
            .then(response => {
                return response.data
            })
            .catch(error => {
                return error
            })
            .then((data) => setData(data))
    }
    return (
        <div>
            <TableHeadNav/>
            <StorageNavigation/>
            <div className={"page-body"}>
                <StorageTable fetchData={fetchData} data={data}/>
            </div>
        </div>
    );
}

export default StoragePage;
