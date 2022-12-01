import React from 'react';
import TableHeadNav from "./TableHeadNav";
import "../css/TableNavigation.css"
import StoringNavigation from "./StoringNavigation";
import StoringTable from "./StoringTable";


function StoringPage() {
    return (
        <div>
            <TableHeadNav/>
            <StoringNavigation/>
            <div className={"page-body"}>
                <StoringTable/>
            </div>
        </div>
    );
}

export default StoringPage;