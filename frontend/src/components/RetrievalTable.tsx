import React from 'react';
import {DrivingOrder} from "../model/DrivingOrder";
import RetrievalTableRow from "./RetrievalTableRow";
import "../css/Table.css"

type retrievalTableProps = {
    drivingOutputOrders: DrivingOrder[]
    fetchRetrievalData: () => void
    setErrorModal: (showErrorAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
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
                        setSuccessMessage={props.setSuccessMessage}
                        setSuccessModal={props.setSuccessModal}
                        setErrorModal={props.setErrorModal}
                        setErrorMessage={props.setErrorMessage}
                        drivingOrder={order}/>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default RetrievalTable;