import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import CheckBoxIcon from '@mui/icons-material/CheckBox';
import {IconButton} from "@mui/material";

type storingTableRowProps = {
    drivingOrder: DrivingOrder
}

function StoringTableRow(props: storingTableRowProps) {

    const deleteDrivingOrder = () => {

    }

    return (
        <tr key={props.drivingOrder.id}>
            <td className="center">{props.drivingOrder.id}</td>
            <td className="center">{props.drivingOrder.itemNumber}</td>
            <td className="center">{props.drivingOrder.storageBinNumber}</td>
            <td className="center">{props.drivingOrder.amount}</td>
            <td className="center"><IconButton aria-label="driving-order-done" size="small">
                <CheckBoxIcon color="success" fontSize="inherit"/>
            </IconButton></td>
        </tr>
    );
}

export default StoringTableRow;