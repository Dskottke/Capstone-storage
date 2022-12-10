import React, {ChangeEvent} from 'react';
import axios from "axios";
import {IconButton} from "@mui/material";
import AddCircleOutlineOutlinedIcon from "@mui/icons-material/AddCircleOutlineOutlined";
import "../css/TableNavigation.css"

type retrievalNavigationProps = {
    fetchRetrievalData: () => void
    retrievalAmountValue: string
    retrievalStorageLocationId: string
    retrievalItemNumber: string
    setRetrievalStorageLocationId: (retrievalStorageBinNumber: string) => void
    setRetrievalItemNumber: (retrievalItemNumber: string) => void
    setRetrievalAmountValue: (retrievalAmount: string) => void
    handleRetrievalAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleRetrievalItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleRetrievalStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
    setRetrievalNavigationErrorModal: (showRetrievalAlert: boolean) => void
    setRetrievalNavigationErrorMessage: (retrievalErrorMessage: string) => void
    setRetrievalNavigationSuccessMessage: (retrievalSuccessMessage: string) => void
    setRetrievalNavigationSuccessModal: (showRetrievalSuccessAlert: boolean) => void
}

function RetrievalNavigation(props: retrievalNavigationProps) {

    const handleSubmit = () => {

        const retrievalItemNumber = props.retrievalItemNumber
        const retrievalStorageLocationId = props.retrievalStorageLocationId
        const retrievalAmount = props.retrievalAmountValue

        axios.post("/api/driving-orders/?type=OUTPUT", {
            itemNumber: retrievalItemNumber,
            storageLocationId: retrievalStorageLocationId,
            amount: retrievalAmount
        })
            .then(response => {
                if (response.status === 201) {
                    props.setRetrievalNavigationSuccessModal(true);
                    props.setRetrievalNavigationSuccessMessage("new driving order created ")
                }
            })
            .catch(error => {
                if (error.response) {
                    props.setRetrievalNavigationErrorModal(true);
                    props.setRetrievalNavigationErrorMessage(error.response.data)
                }
            }).then(props.fetchRetrievalData)
            .then(() => props.setRetrievalStorageLocationId(""))
            .then(() => props.setRetrievalItemNumber(""))
            .then(() => props.setRetrievalAmountValue(""))

    }
    return (
        <div className="topnav">
            <div className={"header-font"} id="navi-logo">retrieval</div>
            <div className="add-container">

                    <input className="item_input_field" value={props.retrievalItemNumber}
                           onChange={props.handleRetrievalItemNumber} type="text" placeholder="item-number"
                           name="item-number"/>
                    <input className="item_input_field" value={props.retrievalStorageLocationId}
                           onChange={props.handleRetrievalStorageBinNumber} type="text" placeholder="storage-bin-nr"
                           name="storage-bin-nr"/>
                    <input className="item_input_field" value={props.retrievalAmountValue}
                           onChange={props.handleRetrievalAmount} type="text" placeholder="amount" name="amount"/>
                    <IconButton onClick={handleSubmit} color={"primary"}><AddCircleOutlineOutlinedIcon sx={{marginTop:"50%"}} color={"inherit"}/></IconButton>

            </div>
        </div>
    );
}

export default RetrievalNavigation;
