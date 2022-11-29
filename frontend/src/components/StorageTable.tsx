import React from 'react';
import {StorageBinModel} from "../model/StorageBinModel";
import StorageTableRow from "./StorageTableRow";

type StorageTableProps = {
    data: StorageBinModel[],
    fetchData: () => void
}

function StorageTable(props: StorageTableProps) {
    return (
        <div className="container-table">
            <table className="RedTable">
                <thead>
                <tr>
                    <th>location-number</th>
                    <th>itemNumber</th>
                    <th>amount</th>
                    <th>notified</th>
                    <th>reserve</th>
                    <th>action</th>
                </tr>
                </thead>
                <tbody>
                {props.data.length === 0 ? (
                    <tr>
                        <td>data not found</td>
                        <td>data not found</td>
                        <td>data not found</td>
                        <td>data not found</td>
                        <td>data not found</td>
                        <td>no action</td>

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