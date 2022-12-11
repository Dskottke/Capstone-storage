import React, {ChangeEvent, useState} from 'react';
import axios from "axios";
import "../css/TableNavigation.css"
import AddCircleOutlineOutlinedIcon from '@mui/icons-material/AddCircleOutlineOutlined';
import {IconButton} from "@mui/material";

type itemNavigationProps = {

    fetchItemData: () => void
    setItemNavigationErrorModal: (showItemErrorAlert: boolean) => void
    setItemNavigationErrorMessage: (itemErrorMessage: string) => void
    setItemNavigationSuccessMessage: (itemSuccessMessage: string) => void
    setItemNavigationSuccessModal: (showItemSuccessAlert: boolean) => void
}

function ItemNavigation(props: itemNavigationProps) {

    const [ean, setEan] = useState("")
    const [itemNumber, setItemNumber] = useState("")
    const [storableValue, setStorableValue] = useState("")

    const handleInputCapacity = (event: ChangeEvent<HTMLInputElement>) => {
        const validCapacity = event.target.value.replace(/\D/g, '')
        setStorableValue(validCapacity)
    }

    const handleInputItemNumber = (event: ChangeEvent<HTMLInputElement>) => {
        const validItemNumber = event.target.value.replace(/\D/g, '')
        setItemNumber(validItemNumber)
    }

    const handleInputEan = (event: ChangeEvent<HTMLInputElement>) => {
        const validEan = event.target.value.replace(/\D/g, '')
        setEan(validEan)
    }

    const handleAddSubmit = () => {

        axios.post("/api/items/", {ean, itemNumber, storableValue})
            .then(response => {
                if (response.status === 201) {
                    props.setItemNavigationSuccessModal(true);
                    props.setItemNavigationSuccessMessage("new item created ")
                }
            })
            .catch(error => {
                    if (error.response) {
                        props.setItemNavigationErrorModal(true);
                        props.setItemNavigationErrorMessage(error.response.data)
                    }
                }
            )
            .then(props.fetchItemData)
            .then(() => setEan(""))
            .then(() => setItemNumber(""))
            .then(() => setStorableValue(""))
    }


    return (
        <div className="topnav">
            <div className="header-font" id="navi-logo">Items</div>
            <div className="add-container">

                    <input className="item_input_field" value={itemNumber}
                           onChange={handleInputItemNumber} type="text" placeholder="item-number" name="item-number"/>
                    <input className="item_input_field" value={ean}
                           onChange={handleInputEan} type="text" placeholder="ean" name="ean"/>
                    <input className="item_input_field" value={storableValue}
                           onChange={handleInputCapacity} type="text" placeholder="capacity" name="capacity"/>
                    <IconButton onClick={handleAddSubmit} color={"primary"}><AddCircleOutlineOutlinedIcon sx={{marginTop:"50%"}} color={"inherit"}/></IconButton>
            </div>
        </div>

    );
}

export default ItemNavigation;
