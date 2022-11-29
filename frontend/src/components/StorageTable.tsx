import React from 'react';
import {StorageBinModel} from "../model/StorageBinModel";
import StorageTableRow from "./StorageTableRow";
import "../css/StorageTable.css"

type StorageTableProps = {
    data: StorageBinModel[],
    fetchData: () => void
}

function StorageTable(props: StorageTableProps) {
    return (
        <div className="container-table">
            <table className="greenTable">
                <thead>
                <tr>
                    <th>location-number</th>
                    <th>itemNumber</th>
                    <th>amount</th>
                </tr>
                </thead>
                <tbody>
                {props.data.length === 0 ? (
                    <tr>
                        <td>data not found</td>
                        <td>data not found</td>
                        <td>data not found</td>
                    </tr>) : (
                    props.data.map(storageBin => <StorageTableRow storageBin={storageBin} key={storageBin.id}
                                                                  fetchData={props.fetchData}></StorageTableRow>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default StorageTable;
