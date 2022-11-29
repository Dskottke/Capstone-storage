import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ItemModel} from "../model/ItemModel";
import ItemNavigation from "./ItemNavigation";
import ItemTable from "./ItemTable";
import "../css/TablePage.css"
import Alert from "@mui/material/Alert";

function ItemPage() {

    const [data, setData] = useState<ItemModel[]>([]);
    const [errorModal, setErrorModal] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")
    const [successModal, setSuccessModal] = useState(false)
    const [successMessage, setSuccessMessage] = useState("")

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axios.get("/api/items/")
            .then(response => {
                return response.data
            })
            .catch(error => {
                return error
            })
            .then((data) => setData(data))
    }

    return (
        <div className={"item-page-container"}>
            <ItemNavigation fetchData={fetchData} setErrorModal={setErrorModal}
                            setErrorMessage={setErrorMessage} setSuccessModal={setSuccessModal}
                            setSuccessMessage={setSuccessMessage}/>
            {errorModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    setErrorModal(false);
                }}>{errorMessage}</Alert>
            }
            {successModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} onClose={() => {
                    setSuccessModal(false);
                }}>{successMessage}</Alert>
            }
            <div className={"page-body"}>
                <ItemTable fetchData={fetchData} data={data} setErrorMessage={setErrorMessage}
                           setErrorModal={setErrorModal} setSuccessMessage={setSuccessMessage}
                           setSuccessModal={setSuccessModal}/>
            </div>

        </div>

    );
}
export default ItemPage;
