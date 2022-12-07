import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import StoringTableRow from "./StoringTableRow";
import "../css/Table.css"

type storingTableProps = {
    fetchData: () => void
    storingDrivingOrders: DrivingOrder[]
    setStoringTableErrorModal: (showStoringErrorAlert: boolean) => void
    setStoringTableErrorMessage: (errorStoringMessage: string) => void
    setStoringTableSuccessMessage: (successStoringMessage: string) => void
    setStoringTableSuccessModal: (showStoringSuccessAlert: boolean) => void
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
                {props.storingDrivingOrders.length === 0 ? (
                    <tr>
                        <td></td>

                    </tr>) : (
                    props.storingDrivingOrders.map(order => <StoringTableRow fetchStoringData={props.fetchData}
                                                                             setStoringTableRowSuccessModal={props.setStoringTableSuccessModal}
                                                                             setStoringTableRowErrorModal={props.setStoringTableErrorModal}
                                                                             setStoringTableRowErrorMessage={props.setStoringTableErrorMessage}
                                                                             setStoringTableRowSuccessMessage={props.setStoringTableSuccessMessage}
                                                                             storingDrivingOrder={order}
                                                                             key={order.id}/>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default StoringTable;
