import React, {ChangeEvent, useState} from 'react';
import {ItemModel} from "../model/ItemModel";
import axios from "axios";
import "../css/ItemTable.css"

type itemPageProbs = {
    data: ItemModel [],
    fetchData: () => void
    setErrorModal: (showAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function ItemTable(props: itemPageProbs) {
    const [inEditMode, setInEditMode] = useState({
        status: true,
        rowKey: ""
    });
    const [storableValue, setStorableValue] = useState<string>("");

    const updateItem = (id: string, Item: ItemModel) => {
        axios.put("/api/items/" + id, Item)
            .then(response => {
                if (response.status === 200) {
                    props.setSuccessModal(true);
                    props.setSuccessMessage("item updated");
                }
            })
            .catch(error => {
                if (error.response.status === 403) {
                    props.setErrorModal(true);
                    props.setErrorMessage("forbidden")
                    ;
                }
            })
            .then(props.fetchData)
            .then(onCancel)
    }
    const deleteItem = (id: string) => {
        axios.delete("/api/items/" + id)
            .then(response => {
                if (response.status === 204) {
                    props.setSuccessModal(true);
                    props.setSuccessMessage("item deleted")
                }
            })
            .catch(error => {
                if (error.response.status === 406) {
                    props.setErrorModal(true);
                    props.setErrorMessage("the item doesn't exist")
                }
            })
            .then(props.fetchData)
    }
    const handleInputCapacity = (event: ChangeEvent<HTMLInputElement>) => {
        const validCapacity = event.target.value.replace(/\D/g, '')
        setStorableValue(validCapacity)
    }
    const findItemById = (id: string) => {
        return props.data.find(item => item.id === id)
    }
    const onSave = (id: string) => {
        let itemToAdd = findItemById(id)
        if (!itemToAdd) {
            return onCancel();
        }

        itemToAdd.storableValue = storableValue;

        updateItem(id, itemToAdd)

    };
    const onCancel = () => {
        setInEditMode({
            status: false,
            rowKey: ""
        })
        setStorableValue("")
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
                    props.data.map((item) => (<tr key={item.id}>
                        <td>{item.itemNumber}</td>
                        <td>{item.ean}</td>
                        <td>{item.name}</td>
                        <td>{item.categoryName}</td>
                        <td>{item.issuingCountry}</td>
                        <td>{inEditMode.status && inEditMode.rowKey === item.id ? (
                            <input value={storableValue}
                                   onChange={handleInputCapacity}
                            />) : (
                            item.storableValue)}
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
                                >Edit
                                </button>
                                <button
                                    className="button-right"
                                    onClick={() => deleteItem(item.id)}
                                >delete
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
