import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";

type storingTableRowProps = {
    drivingOrder: DrivingOrder
}

function StoringTableRow(props: storingTableRowProps) {
    return (
        <tr key={props.drivingOrder.id}>
            <td className="center">{props.drivingOrder.id}</td>
            <td className="center">{props.drivingOrder.itemNumber}</td>
            <td className="center">{props.drivingOrder.storageBinNumber}</td>
            <td className="center">{props.drivingOrder.amount}</td>
            <td></td>
        </tr>
    );
}

export default StoringTableRow;