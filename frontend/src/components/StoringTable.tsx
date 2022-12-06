import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import StoringTableRow from "./StoringTableRow";
import "../css/Table.css"

type storingTableProps = {
    fetchData: () => void
    storingDrivingOrders: DrivingOrder[]
    setStoringErrorModal: (showStoringErrorAlert: boolean) => void
    setStoringErrorMessage: (errorStoringMessage: string) => void
    setStoringSuccessMessage: (successStoringMessage: string) => void
    setStoringSuccessModal: (showStoringSuccessAlert: boolean) => void
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
                                                                             setStoringSuccessModal={props.setStoringSuccessModal}
                                                                             setStoringErrorModal={props.setStoringErrorModal}
                                                                             setStoringErrorMessage={props.setStoringErrorMessage}
                                                                             setStoringSuccessMessage={props.setStoringSuccessMessage}
                                                                             storingDrivingOrder={order}
                                                                             key={order.id}/>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default StoringTable;