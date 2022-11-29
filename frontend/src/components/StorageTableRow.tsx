import React from 'react';
import {StorageBinModel} from "../model/StorageBinModel";

type StorageTableRowProps = {
    storageBin: StorageBinModel
    fetchData: () => void
}
function StorageTableRow({storageBin, fetchData}: StorageTableRowProps) {

    return (
        <tr key={storageBin.id}>
            <td>{storageBin.id}</td>
            <td>{storageBin.itemNumber}</td>
            <td>{storageBin.amount}</td>
        </tr>
    );
}

export default StorageTableRow;
