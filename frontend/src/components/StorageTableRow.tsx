import React from 'react';
import {StorageBinModel} from "../model/StorageBinModel";

type StorageTableRowProps = {
    storageBin: StorageBinModel
}
function StorageTableRow({storageBin}: StorageTableRowProps) {

    return (
        <tr key={storageBin.id}>
            <td className="center">{storageBin.locationId}</td>
            <td className="center">{storageBin.itemNumber}</td>
            <td>{storageBin.storedItemName}</td>
            <td className="center">{storageBin.amount}</td>
        </tr>
    );
}

export default StorageTableRow;
