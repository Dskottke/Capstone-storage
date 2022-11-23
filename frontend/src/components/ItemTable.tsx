import React, {useState} from 'react';
import {ItemModel} from "../model/ItemModel";
import axios from "axios";
import "../css/ItemTable.css"

type itemPageProbs = {
    data: ItemModel [],
    fetchData: () => void
}

function ItemTable(props: itemPageProbs) {
    const [inEditMode, setInEditMode] = useState({
        status: true,
        rowKey: ""
    });
    const [valueStoreable, setValueStoreable] = useState<string>();

    const updateItem = (id: string, Item: ItemModel) => {
        axios.put("/api/items/" + id, Item)
            .catch(error => {
                return error
            })
            .then(props.fetchData)
            .then(onCancel)

    }
    const deleteItem = (id: string) => {
        axios.delete("/api/items/" + id)
            .catch(error => {
                return error
            })
            .then(props.fetchData)

    }
    const findItemById = (id: string) => {
        return props.data.find(item => item.id === id)
    }
    const onSave = (id: string) => {
        let itemToAdd = findItemById(id)
        if (itemToAdd === undefined) {
            return onCancel();
        }
        if (typeof valueStoreable === "string") {
            itemToAdd.storeableValue = valueStoreable;
        }
        updateItem(id, itemToAdd)

    };
    const onCancel = () => {
        setInEditMode({
            status: false,
            rowKey: ""
        })
        setValueStoreable("")
    }
    const onEdit = (id: string) => {
        setInEditMode({
            status: true,
            rowKey: id
        })
    }
    return (
        <div className="container-table">
            <table className="blueTable">
                <thead>
                <tr>
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
                    props.data.map((item) => (<tr key={item.id}>
                        <td>{item.ean}</td>
                        <td>{item.name}</td>
                        <td>{item.categoryName}</td>
                        <td>{item.issuingCountry}</td>
                        <td>{inEditMode.status && inEditMode.rowKey === item.id ? (
                            <input value={valueStoreable}
                                   onChange={event => setValueStoreable(event.target.value)}
                            />) : (
                            item.storeableValue)}

                        </td>
                        <td width="200px">{inEditMode.status && inEditMode.rowKey === item.id ? (
                                <React.Fragment>
                                    <button
                                        className={"button-left"}

                                        onClick={() => onSave(item.id)}

                                    >save
                                    </button>
                                    <button
                                        className={"button-right"}
                                        style={{marginLeft: 8}}
                                        onClick={() => onCancel()}
                                    >cancel
                                    </button>
                                </React.Fragment>) :
                            <React.Fragment>

                                <button
                                    className={"button-left"}
                                    onClick={() => onEdit(item.id)}
                                >
                                    Edit
                                </button>
                                <button
                                    className="button-right"
                                    onClick={() => deleteItem(item.id)}
                                >
                                    delete
                                </button>


                            </React.Fragment>
                        }

                        </td>
                    </tr>))
                )}
                </tbody>
            </table>
        </div>
    )
}
export default ItemTable;
