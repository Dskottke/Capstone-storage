import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import StoringTableRow from "./StoringTableRow";
import "../css/table.css"

type storingTableProps = {
    drivingOrders: DrivingOrder[]
}

function StoringTable(props: storingTableProps) {
    return (
        <div className="container-table">
            <table id="storing-table" className="Table">
                <thead>
                <tr>
                    <th className="center">nr.</th>
                    <th className="center">item-nr.</th>
                    <th className="center">storage-bin-nr.</th>
                    <th className="center">amount</th>
                    <th className="center">action</th>
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