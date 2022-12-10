import React, {ChangeEvent} from 'react';
import axios from "axios";
import {IconButton} from "@mui/material";
import AddCircleOutlineOutlinedIcon from "@mui/icons-material/AddCircleOutlineOutlined";

type storingNavigationProps = {
    fetchStoringData: () => void
    storingAmountValue: string
    storingStorageLocationId: string
    storingItemNumber: string
    setStoringStorageBinNumber: (storingStorageBinNumber: string) => void
    setStoringItemNumber: (storingItemNumber: string) => void
    setStoringAmountValue: (storingAmount: string) => void
    handleStoringAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleStoringItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleStoringStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
    setStoringNavigationErrorModal: (showStoringAlert: boolean) => void
    setStoringNavigationErrorMessage: (errorStoringMessage: string) => void
    setStoringNavigationSuccessMessage: (successStoringMessage: string) => void
    setStoringNavigationSuccessModal: (showStoringSuccessAlert: boolean) => void
}

function StoringNavigation(props: storingNavigationProps) {

    const handleAddSubmit = () => {
        const storingItemNumber = props.storingItemNumber
        const storingStorageLocationId = props.storingStorageLocationId
        const storingAmount = props.storingAmountValue

        axios.post("/api/driving-orders/?type=INPUT", {
            itemNumber: storingItemNumber,
            storageLocationId: storingStorageLocationId,
            amount: storingAmount
        })
            .then(response => {
                if (response.status === 201) {
                    props.setStoringNavigationSuccessModal(true);
                    props.setStoringNavigationSuccessMessage("new driving order created ")
                }
            })
            .catch(error => {
                if (error.response) {
                    props.setStoringNavigationErrorModal(true);
                    props.setStoringNavigationErrorMessage(error.response.data)
                }
            }).then(props.fetchStoringData)
            .then(() => props.setStoringAmountValue(""))
            .then(() => props.setStoringItemNumber(""))
            .then(() => props.setStoringStorageBinNumber(""))
    }


    return (
        <div className="topnav">
            <div className="header-font" id="navi-logo">storing</div>
            <div className="add-container">

                    <input className="item_input_field" value={props.storingItemNumber}
                           onChange={props.handleStoringItemNumber} type="text" placeholder="item-number"
                           name="item-number"/>
                    <input className="item_input_field" value={props.storingStorageLocationId}
                           onChange={props.handleStoringStorageBinNumber} type="text" placeholder="storage-bin-nr"
                           name="ean"/>
                    <input className="item_input_field" value={props.storingAmountValue}
                           onChange={props.handleStoringAmount} type="text" placeholder="amount" name="amount"/>
                    <IconButton onClick={handleAddSubmit} color={"primary"}><AddCircleOutlineOutlinedIcon sx={{marginTop:"50%"}} color={"inherit"}/></IconButton>
            </div>
        </div>

    );
}

export default StoringNavigation;
