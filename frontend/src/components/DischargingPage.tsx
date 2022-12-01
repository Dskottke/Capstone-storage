import React from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import DischargingNavigation from "./DischargingNavigation";
import DischargingTable from "./DischargingTable";

function DischargingPage() {
    return (
        <div>
            <TableHeadNav/>
            <DischargingNavigation/>
            <div className={"page-body"}>
                <DischargingTable/>
            </div>
        </div>
    );
}


export default DischargingPage;