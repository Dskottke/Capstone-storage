import React, {useEffect, useState} from 'react';
import axios from "axios";
import {StorageBinModel} from "../model/StorageBinModel";
import "../css/TablePage.css"
import StorageTable from "./StorageTable";
import StorageNavigation from "./StorageNavigation";
import TableHeadNav from "./TableHeadNav";

function StoragePage() {

    const [storageData, setStorageData] = useState<StorageBinModel[]>([]);

    useEffect(() => {
        fetchStorageData()
    }, [])

    const fetchStorageData = () => {
        axios.get("/api/storagebins/")
            .then(response => {
                return response.data
            })
            .catch(error => {
                return error
            })
            .then((data) => setStorageData(data))
    }
    return (
        <div className={"page-container"}>
            <TableHeadNav/>
            <StorageNavigation/>
            <div className={"page-body"}>
                <StorageTable fetchStorageData={fetchStorageData} storageData={storageData}/>
            </div>
        </div>
    );
}

export default StoragePage;
