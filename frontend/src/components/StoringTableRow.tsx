import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import {IconButton} from "@mui/material";
import axios from "axios";

type storingTableRowProps = {
    fetchData: () => void
    drivingOrder: DrivingOrder
    setErrorModal: (showErrorAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function StoringTableRow(props: storingTableRowProps) {

    const deleteDrivingOrder = () => {
        axios.delete("/api/driving-orders/input/" + props.drivingOrder.id)
            .then(response => {
                if (response.status === 204) {
                    props.setSuccessModal(true);
                    props.setSuccessMessage("items stored")
                }
            })
            .catch(error => {
                if (error.response) {
                    props.setErrorModal(true);
                    props.setErrorMessage(error.response.data)
                }
            })
            .then(props.fetchData)

    }

    return (
        <tr key={props.drivingOrder.id}>
            <td className="center">{props.drivingOrder.id}</td>
            <td className="center">{props.drivingOrder.itemNumber}</td>
            <td className="center">{props.drivingOrder.storageLocationId}</td>
            <td className="center">{props.drivingOrder.amount}</td>
            <td className="center"><IconButton aria-label="driving-order-done" size="small"
                                               onClick={deleteDrivingOrder}>
                <CheckBoxIcon color="success" fontSize="inherit"/>
            </IconButton></td>
        </tr>
    );
}

export default StoringTableRow;