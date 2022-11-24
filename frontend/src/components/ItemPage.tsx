import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ItemModel} from "../model/ItemModel";
import ItemNavigation from "./ItemNavigation";
import ItemTable from "./ItemTable";
import "../css/ItemPage.css"
import Alert from "@mui/material/Alert";

function ItemPage() {
    const [data, setData] = useState<ItemModel[]>([]);
    const [failModal, setFailModal] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axios.get("/api/items/")
            .then(response => {
                    return response.data
                }
            ).catch(error => {
            return error
        })
            .then((data) => setData(data))
    }
    return (
        <div className={"item-page-container"}>
            <ItemNavigation fetchData={fetchData} setFailModal={setFailModal} setErrorMessage={setErrorMessage}/>
            {failModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    setFailModal(false)
                }}>{errorMessage}</Alert>
            }
            <div className={"item-page-body"}>
                <ItemTable fetchData={fetchData} data={data} setErrorMessage={setErrorMessage}
                           setFailModal={setFailModal}/>
            </div>

        </div>

    );
}
export default ItemPage;
