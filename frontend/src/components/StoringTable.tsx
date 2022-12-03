import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";

import StoringTableRow from "./StoringTableRow";

type storingTableProps = {
    drivingOrders: DrivingOrder[]
}

function StoringTable(props: storingTableProps) {
    return (
        <div className="container-table">
            <table className="Table">
                <thead>
                <tr>
                    <th className={"center"}>Nr</th>
                    <th>storage-bin</th>
                    <th>item</th>
                    <th>amount</th>
                    <th>action</th>
                </tr>
                </thead>
                <tbody>
                {props.drivingOrders.length === 0 ? (
                    <tr>
                        <td></td>

                    </tr>) : (
                    props.drivingOrders.map(order => <StoringTableRow drivingOrder={order} key={order.id}/>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default StoringTable;