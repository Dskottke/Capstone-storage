import React from 'react';


function StoringTable() {
    return (
        <div className="container-table">
            <table className="Table">
                <thead>
                <tr>
                    <th>Nr</th>
                    <th>storage-bin</th>
                    <th>item</th>
                    <th>amount</th>
                    <th>action</th>
                </tr>
                </thead>
            </table>
        </div>
    );
}

export default StoringTable;