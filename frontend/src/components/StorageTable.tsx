import React from 'react';
import {StorageBinModel} from "../model/StorageBinModel";
import StorageTableRow from "./StorageTableRow";
import "../css/Table.css"

type StorageTableProps = {
    data: StorageBinModel[],
    fetchData: () => void
}

function StorageTable(props: StorageTableProps) {
    return (
        <div className="container-table">
            <table id="storage_table" className="Table">
                <thead>
                <tr>
                    <th className="center">location-number</th>
                    <th className="center">item-nr.</th>
                    <th className="center">amount</th>
                </tr>
                </thead>
                <tbody>
                {props.data.length === 0 ? (
                    <tr>
                        <td></td>
                    </tr>) : (
                    props.data.map(storageBin => <StorageTableRow storageBin={storageBin} key={storageBin.id}
                        ></StorageTableRow>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default StorageTable;
