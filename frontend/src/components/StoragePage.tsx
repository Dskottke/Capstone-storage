import React, {useEffect, useState} from 'react';
import axios from "axios";
import {StorageBinModel} from "../model/StorageBinModel";
import "../css/TablePage.css"
import StorageTable from "./StorageTable";
import StorageNavigation from "./StorageNavigation";

function StoragePage() {

    const [data, setData] = useState<StorageBinModel[]>([]);

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axios.get("/api/storage/")
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
            <StorageNavigation/>
            <div className={"page-body"}>
                <StorageTable fetchData={fetchData} data={data}/>
            </div>
        </div>
    );
}

export default StoragePage;
