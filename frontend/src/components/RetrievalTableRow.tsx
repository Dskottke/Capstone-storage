import React from 'react';

import {DrivingOrder} from "../model/DrivingOrder";
import {IconButton} from "@mui/material";
import CheckBoxIcon from "@mui/icons-material/CheckBox";


type dischargingTableRowProps = {
    drivingOrder: DrivingOrder
    fetchRetrievalData: () => void
    setRetrievalTableRowErrorModal: (showRetrievalErrorAlert: boolean) => void
    setRetrievalTableRowMessage: (retrievalErrorMessage: string) => void
    setRetrievalTableRowSuccessMessage: (retrievalSuccessMessage: string) => void
    setRetrievalTableRowSuccessModal: (showRetrievalSuccessAlert: boolean) => void
}

function RetrievalTableRow(props: dischargingTableRowProps) {
    return (
        <tr key={props.drivingOrder.id}>
            <td className="center">{props.drivingOrder.id}</td>
            <td className="center">{props.drivingOrder.itemNumber}</td>
            <td className="center">{props.drivingOrder.storageLocationId}</td>
            <td className="center">{props.drivingOrder.amount}</td>
            <td className="center"><IconButton aria-label="driving-order-done" size="small">
                <CheckBoxIcon color="success" fontSize="inherit"/>
            </IconButton></td>
        </tr>
    );
}

export default RetrievalTableRow;