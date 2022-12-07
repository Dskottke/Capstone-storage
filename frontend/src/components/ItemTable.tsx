import {ItemModel} from "../model/ItemModel";
import "../css/Table.css"
import ItemTableRow from "./ItemTableRow";

type itemPageProps = {
    itemData: ItemModel[],
    fetchItemData: () => void
    setItemTableErrorModal: (showItemAlert: boolean) => void
    setItemTableErrorMessage: (itemErrorMessage: string) => void
    setItemTableSuccessMessage: (itemSuccessMessage: string) => void
    setItemTableSuccessModal: (showItemSuccessAlert: boolean) => void
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
                    <th className="center">stored-items</th>
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
                                                             setItemTableRowErrorMessage={props.setItemTableErrorMessage}
                                                             setItemTableRowErrorModal={props.setItemTableErrorModal}
                                                             setItemTableRowSuccessMessage={props.setItemTableSuccessMessage}
                                                             setItemTableRowSuccessModal={props.setItemTableSuccessModal}/>
                    ))}
                </tbody>
            </table>
        </div>
    )
}

export default ItemTable;
