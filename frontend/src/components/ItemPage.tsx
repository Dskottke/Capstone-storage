import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ItemModel} from "../model/ItemModel";
import ItemNavigation from "./ItemNavigation";
import ItemTable from "./ItemTable";
import "../css/TablePage.css"
import Alert from "@mui/material/Alert";
import TableHeadNav from "./TableHeadNav";

type itemPageProps = {
    errorModal: boolean
    successModal: boolean
    errorMessage: string
    successMessage: string
    setErrorModal: (showAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function ItemPage(props: itemPageProps) {

    const [data, setData] = useState<ItemModel[]>([]);


    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axios.get("/api/items/")
            .then(response => {
                return response.data
            })
            .catch(error => {
               console.error(error)
            })
            .then((data) => setData(data))
    }

    return (
        <div className={"page-container"}>
            <TableHeadNav/>
            <ItemNavigation fetchData={fetchData} setErrorModal={props.setErrorModal}
                            setErrorMessage={props.setErrorMessage} setSuccessModal={props.setSuccessModal}
                            setSuccessMessage={props.setSuccessMessage}/>
            {props.errorModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    props.setErrorModal(false);
                }}>{props.errorMessage}</Alert>
            }
            {props.successModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} onClose={() => {
                    props.setSuccessModal(false);
                }}>{props.successMessage}</Alert>
            }
            <div className={"page-body"}>
                <ItemTable fetchData={fetchData} data={data} setErrorMessage={props.setErrorMessage}
                           setErrorModal={props.setErrorModal} setSuccessMessage={props.setSuccessMessage}
                           setSuccessModal={props.setSuccessModal}/>
            </div>

        </div>

    );
}
export default ItemPage;
