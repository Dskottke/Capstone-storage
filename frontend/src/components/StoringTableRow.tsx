import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import {IconButton} from "@mui/material";
import axios from "axios";

type storingTableRowProps = {
    fetchStoringData: () => void
    storingDrivingOrder: DrivingOrder
    setStoringErrorModal: (showErrorAlert: boolean) => void
    setStoringErrorMessage: (errorMessage: string) => void
    setStoringSuccessMessage: (successMessage: string) => void
    setStoringSuccessModal: (showSuccessAlert: boolean) => void
}

function StoringTableRow(props: storingTableRowProps) {

    const deleteDrivingOrder = () => {
        axios.delete("/api/driving-orders/input/" + props.storingDrivingOrder.id)
            .then(response => {
                if (response.status === 204) {
                    props.setStoringSuccessModal(true);
                    props.setStoringSuccessMessage("items stored")
                }
            })
            .catch(error => {
                if (error.response) {
                    props.setStoringErrorModal(true);
                    props.setStoringErrorMessage(error.response.data)
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
                                               onClick={deleteDrivingOrder}>
                <CheckBoxIcon color="success" fontSize="inherit"/>
            </IconButton></td>
        </tr>
    );
}

export default StoringTableRow;