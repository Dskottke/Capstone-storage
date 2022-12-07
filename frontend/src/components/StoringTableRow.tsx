import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import {IconButton} from "@mui/material";
import axios from "axios";

type storingTableRowProps = {
    fetchStoringData: () => void
    storingDrivingOrder: DrivingOrder
    setStoringTableRowErrorModal: (showStoringErrorAlert: boolean) => void
    setStoringTableRowErrorMessage: (errorStoringMessage: string) => void
    setStoringTableRowSuccessMessage: (successStoringMessage: string) => void
    setStoringTableRowSuccessModal: (showStoringSuccessAlert: boolean) => void
}

function StoringTableRow(props: storingTableRowProps) {

    const finishInputDrivingOrder = () => {
        axios.delete("/api/driving-orders/input/" + props.storingDrivingOrder.id)
            .then(response => {
                if (response.status === 204) {
                    props.setStoringTableRowSuccessModal(true);
                    props.setStoringTableRowSuccessMessage("items stored")
                }
            })
            .catch(error => {
                if (error.response) {
                    props.setStoringTableRowErrorModal(true);
                    props.setStoringTableRowErrorMessage(error.response.data)
                }
            })
            .then(props.fetchStoringData)

    }

    return (
        <tr key={props.storingDrivingOrder.id}>
            <td className="center">{props.storingDrivingOrder.id}</td>
            <td className="center">{props.storingDrivingOrder.itemNumber}</td>
            <td className="center">{props.storingDrivingOrder.storageLocationId}</td>
            <td className="center">{props.storingDrivingOrder.amount}</td>
            <td className="center"><IconButton aria-label="driving-order-done" size="small"
                                               onClick={finishInputDrivingOrder}>
                <CheckBoxIcon color="success" fontSize="inherit"/>
            </IconButton></td>
        </tr>
    );
}

export default StoringTableRow;
