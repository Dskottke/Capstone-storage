import React from 'react';

import {DrivingOrder} from "../model/DrivingOrder";
import {IconButton} from "@mui/material";
import CheckBoxIcon from "@mui/icons-material/CheckBox";
import axios from "axios";


type dischargingTableRowProps = {
    retrievalDrivingOrder: DrivingOrder
    fetchRetrievalData: () => void
    setRetrievalTableRowErrorModal: (showRetrievalErrorAlert: boolean) => void
    setRetrievalTableRowErrorMessage: (retrievalErrorMessage: string) => void
    setRetrievalTableRowSuccessMessage: (retrievalSuccessMessage: string) => void
    setRetrievalTableRowSuccessModal: (showRetrievalSuccessAlert: boolean) => void
}

function RetrievalTableRow(props: dischargingTableRowProps) {

    const finishOutputDrivingOrder = () => {
        axios.delete("/api/driving-orders/output/" + props.retrievalDrivingOrder.id)
            .then(response => {
                if (response.status === 204) {
                    props.setRetrievalTableRowSuccessModal(true);
                    props.setRetrievalTableRowSuccessMessage("items retrieved")
                }
            })
            .catch(error => {
                if (error.response) {
                    props.setRetrievalTableRowErrorModal(true);
                    props.setRetrievalTableRowErrorMessage(error.response.data)
                }
            })
            .then(props.fetchRetrievalData)
    }

    return (
        <tr key={props.retrievalDrivingOrder.id}>
            <td className="center">{props.retrievalDrivingOrder.id}</td>
            <td className="center">{props.retrievalDrivingOrder.itemNumber}</td>
            <td className="center">{props.retrievalDrivingOrder.storageLocationId}</td>
            <td className="center">{props.retrievalDrivingOrder.amount}</td>
            <td className="center"><IconButton sx={{position:"static"}}  aria-label="driving-order-done" size="small"
                                               onClick={finishOutputDrivingOrder}>
                <CheckBoxIcon color="primary" fontSize="inherit"/>
            </IconButton></td>
        </tr>
    );
}

export default RetrievalTableRow;
