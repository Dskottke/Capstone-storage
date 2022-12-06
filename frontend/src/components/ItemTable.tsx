import {ItemModel} from "../model/ItemModel";
import "../css/Table.css"
import ItemTableRow from "./ItemTableRow";

type itemPageProps = {
    itemData: ItemModel[],
    fetchItemData: () => void
    setItemErrorModal: (showAlert: boolean) => void
    setItemErrorMessage: (errorMessage: string) => void
    setItemSuccessMessage: (successMessage: string) => void
    setItemSuccessModal: (showSuccessAlert: boolean) => void
}

function ItemTable(props: itemPageProps) {
    return (
        <div className="container-table">
            <table className="Table">
                <thead>
                <tr>
                    <th className="center" id="id_head">Nr</th>
                    <th className="center">ean</th>
                    <th>name</th>
                    <th>category</th>
                    <th>issuing country</th>
                    <th className="center" id="capacity_head">capacity</th>
                    <th className="center" id="action_head">action</th>
                </tr>
                </thead>
                <tbody>
                {props.itemData.length === 0 ? (
                    <tr>
                        <td></td>

                    </tr>) : (
                    props.itemData.map(item => <ItemTableRow item={item} key={item.id}
                                                             fetchItemData={props.fetchItemData}
                                                             setItemErrorMessage={props.setItemErrorMessage}
                                                             setItemErrorModal={props.setItemErrorModal}
                                                             setItemSuccessMessage={props.setItemSuccessMessage}
                                                             setItemSuccessModal={props.setItemSuccessModal}/>
                    ))}
                </tbody>
            </table>
        </div>
    )
}

export default ItemTable;
