import React, {useState} from 'react';
import axios from "axios";
import {StorageBinModel} from "../model/StorageBinModel";
import "../css/TablePage.css"
import StorageTable from "./StorageTable";

function StoragePage() {

    const [data, setData] = useState<StorageBinModel[]>([]);

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
        <div className={"page-body"}>
            <StorageTable fetchData={fetchData} data={data}/>
        </div>
    );
}

export default StoragePage;