import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import RetrievalTableRow from "./RetrievalTableRow";
import "../css/Table.css"

type retrievalTableProps = {
    drivingOutputOrders: DrivingOrder[]
    fetchRetrievalData: () => void
    setRetrievalErrorModal: (showErrorAlert: boolean) => void
    setRetrievalErrorMessage: (errorMessage: string) => void
    setRetrievalSuccessMessage: (successMessage: string) => void
    setRetrievalSuccessModal: (showSuccessAlert: boolean) => void
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
                        setSuccessMessage={props.setRetrievalSuccessMessage}
                        setSuccessModal={props.setRetrievalSuccessModal}
                        setErrorModal={props.setRetrievalErrorModal}
                        setErrorMessage={props.setRetrievalErrorMessage}
                        drivingOrder={order}/>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default RetrievalTable;