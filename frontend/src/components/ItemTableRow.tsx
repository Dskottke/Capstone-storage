import React, {ChangeEvent, useState} from 'react';
import {ItemModel} from "../model/ItemModel";
import axios from "axios";

type ItemTableRowProps = {
    item: ItemModel
    fetchData: () => void
    setErrorModal: (showErrorAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function ItemTableRow({
                          item,
                          fetchData,
                          setSuccessModal,
                          setSuccessMessage,
                          setErrorMessage,
                          setErrorModal
                      }: ItemTableRowProps) {

    const [inEditMode, setInEditMode] = useState({
        status: true,
        rowKey: ""
    });

    const [storableValue, setStorableValue] = useState<string>("");

    const updateItem = () => {


        axios.put("/api/items/" + item.id, {...item, storableValue})
            .then(response => {
                if (response.status === 200) {
                    setSuccessModal(true);
                    setSuccessMessage("item updated");
                }
            })
            .catch(error => {
                if (error.response.status === 403) {
                    setErrorModal(true);
                    setErrorMessage("forbidden")
                    ;
                }
            })
            .then(fetchData)
            .then(onCancel)
    }
    const deleteItem = (id: string) => {
        axios.delete("/api/items/" + id)
            .then(response => {
                if (response.status === 204) {
                    setSuccessModal(true);
                    setSuccessMessage("item deleted")
                }
            })
            .catch(error => {
                if (error.response.status === 406) {
                    setErrorModal(true);
                    setErrorMessage("the item doesn't exist")
                }
            })
            .then(fetchData)
    }
    const handleInputCapacity = (event: ChangeEvent<HTMLInputElement>) => {
        const validCapacity = event.target.value.replace(/\D/g, '')
        setStorableValue(validCapacity)
    }


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
        <tr key={item.id}>
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
                            onClick={() => updateItem()}
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
        </tr>
    );
}

export default ItemTableRow;
