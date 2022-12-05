import {ItemModel} from "../model/ItemModel";
import "../css/table.css"
import ItemTableRow from "./ItemTableRow";

type itemPageProps = {
    data: ItemModel[],
    fetchData: () => void
    setErrorModal: (showAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
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
                {props.data.length === 0 ? (
                    <tr>
                        <td></td>

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
