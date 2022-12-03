import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import RetrievalTableRow from "./RetrievalTableRow";

type dischargingTableProps = {
    drivingOrders: DrivingOrder[]
}

function RetrievalTable(props: dischargingTableProps) {

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
                    props.drivingOrders.map(order => <RetrievalTableRow drivingOrder={order}/>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default RetrievalTable;