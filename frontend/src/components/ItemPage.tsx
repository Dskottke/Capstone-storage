import React, {useEffect, useState} from 'react';
import axios from "axios";
import {ItemModel} from "../model/ItemModel";
import ItemNavigation from "./ItemNavigation";
import ItemTable from "./ItemTable";
import "../css/TablePage.css"
import Alert from "@mui/material/Alert";
import TableHeadNav from "./TableHeadNav";

type itemPageProps = {
    itemErrorModal: boolean
    itemSuccessModal: boolean
    itemErrorMessage: string
    itemSuccessMessage: string
    setItemErrorModal: (showAlert: boolean) => void
    setItemErrorMessage: (errorMessage: string) => void
    setItemSuccessMessage: (successMessage: string) => void
    setItemSuccessModal: (showSuccessAlert: boolean) => void
}

function ItemPage(props: itemPageProps) {

    const [itemData, setItemData] = useState<ItemModel[]>([]);
    
    useEffect(() => {
        fetchItemData()
    }, [])

    const fetchItemData = () => {
        axios.get("/api/items/")
            .then(response => {
                return response.data
            })
            .catch(error => {
                console.error(error)
            })
            .then((data) => setItemData(data))
    }

    return (
        <div className={"page-container"}>
            <TableHeadNav/>
            <ItemNavigation fetchItemData={fetchItemData} setItemErrorModal={props.setItemErrorModal}
                            setItemErrorMessage={props.setItemErrorMessage}
                            setItemSuccessModal={props.setItemSuccessModal}
                            setItemSuccessMessage={props.setItemSuccessMessage}/>
            {props.itemErrorModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} severity="error" onClose={() => {
                    props.setItemErrorModal(false);
                }}>{props.itemErrorMessage}</Alert>
            }
            {props.itemSuccessModal &&
                <Alert style={{width: '80%', marginLeft: "10%", marginTop: "30px"}} onClose={() => {
                    props.setItemSuccessModal(false);
                }}>{props.itemSuccessMessage}</Alert>
            }
            <div className={"page-body"}>
                <ItemTable fetchItemData={fetchItemData} itemData={itemData}
                           setItemErrorMessage={props.setItemErrorMessage}
                           setItemErrorModal={props.setItemErrorModal}
                           setItemSuccessMessage={props.setItemSuccessMessage}
                           setItemSuccessModal={props.setItemSuccessModal}/>
            </div>

        </div>

    );
}
export default ItemPage;
