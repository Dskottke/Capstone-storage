import React from 'react';
import {StorageBinModel} from "../model/StorageBinModel";

type StorageTableRowProps = {
    storageBin: StorageBinModel
    fetchData: () => void
}
function StorageTableRow({storageBin, fetchData}: StorageTableRowProps) {

    return (
        <tr key={storageBin.id}>
            <td className="center">{storageBin.id}</td>
            <td className="center">{storageBin.itemNumber}</td>
            <td className="center">{storageBin.amount}</td>
        </tr>
    );
}

export default StorageTableRow;
