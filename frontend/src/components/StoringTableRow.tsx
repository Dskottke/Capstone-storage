import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";

type storingTableRowProps = {
    drivingOrder: DrivingOrder
}

function StoringTableRow(props: storingTableRowProps) {
    return (
        <tr key={props.drivingOrder.id}>
            <td className="center">{props.drivingOrder.id}</td>
            <td>{props.drivingOrder.storageBinNumber}</td>
            <td>{props.drivingOrder.itemNumber}</td>
            <td>{props.drivingOrder.amount}</td>
        </tr>
    );
}

export default StoringTableRow;