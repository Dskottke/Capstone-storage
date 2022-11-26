import {ItemModel} from "../model/ItemModel";

import "../css/ItemTable.css"
import ItemTableRow from "./ItemTableRow";

type itemPageProbs = {
    data: ItemModel[],
    fetchData: () => void
    setErrorModal: (showAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function ItemTable(props: itemPageProbs) {

    return (
        <div className="container-table">
            <table className="blueTable">
                <thead>
                <tr>
                    <th>item-Nr</th>
                    <th>ean</th>
                    <th>name</th>
                    <th>category</th>
                    <th>issuing country</th>
                    <th>capacity</th>
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
                        <td>data not found</td>
                    </tr>) : (
                    props.data.map(item => <ItemTableRow item={item} key={item.id}
                                                         fetchData={props.fetchData}
                                                         setErrorMessage={props.setErrorMessage}
                                                         setErrorModal={props.setErrorModal}
                                                         setSuccessMessage={props.setSuccessMessage}
                                                         setSuccessModal={props.setSuccessModal}/>
                    ))}
                </tbody>
            </table>
        </div>
    )
}

export default ItemTable;
