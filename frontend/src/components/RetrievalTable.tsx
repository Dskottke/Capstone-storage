import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import RetrievalTableRow from "./RetrievalTableRow";
import "../css/Table.css"

type retrievalTableProps = {
    drivingOutputOrders: DrivingOrder[]
    fetchRetrievalData: () => void
    setRetrievalTableErrorModal: (showRetrievalErrorAlert: boolean) => void
    setRetrievalTableErrorMessage: (retrievalErrorMessage: string) => void
    setRetrievalTableSuccessMessage: (retrievalSuccessMessage: string) => void
    setRetrievalTableSuccessModal: (showRetrievalSuccessAlert: boolean) => void
}

function RetrievalTable(props: retrievalTableProps) {

    return (
        <div className="container-table">
            <table className="Table" id="retrieval-table">
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
                {props.drivingOutputOrders.length === 0 ? (
                    <tr>
                        <td></td>

                    </tr>) : (
                    props.drivingOutputOrders.map(order => <RetrievalTableRow
                        fetchRetrievalData={props.fetchRetrievalData}
                        setRetrievalTableRowSuccessMessage={props.setRetrievalTableSuccessMessage}
                        setRetrievalTableRowSuccessModal={props.setRetrievalTableSuccessModal}
                        setRetrievalTableRowErrorModal={props.setRetrievalTableErrorModal}
                        setRetrievalTableRowErrorMessage={props.setRetrievalTableErrorMessage}
                        retrievalDrivingOrder={order}/>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default RetrievalTable;