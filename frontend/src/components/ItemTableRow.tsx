import React, {ChangeEvent, useState} from 'react';
import {ItemModel} from "../model/ItemModel";
import axios from "axios";
import "../css/ItemTableRow.css"

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

    const [storableValue, setStorableValue] = useState<string>("");
    const [onEditOpen, setOnEditOpen] = useState(false);

    const updateItem = () => {
        axios.put("/api/items/" + item.id, {...item, storableValue})
            .then(response => {
                if (response.status === 200) {
                    setSuccessModal(true);
                    setSuccessMessage("item updated");
                }
            })
            .catch(error => {
                if (error.response) {
                    setErrorModal(true);
                    setErrorMessage(error.response.data)
                    ;
                }
            })
            .then(fetchData)
            .then(() => setOnEditOpen(!onEditOpen))
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
                if (error.response) {
                    setErrorModal(true);
                    setErrorMessage(error.response.data)
                }
            })
            .then(fetchData)
    }
    const handleInputCapacity = (event: ChangeEvent<HTMLInputElement>) => {
        const validCapacity = event.target.value.replace(/\D/g, '')
        setStorableValue(validCapacity)
    }


    return (

        <tr key={item.id}>
            <td>{item.itemNumber}</td>
            <td>{item.ean}</td>
            <td>{item.name}</td>
            <td>{item.categoryName}</td>
            <td>{item.issuingCountry}</td>
            <td>{onEditOpen ? (
                <input value={storableValue}
                       onChange={handleInputCapacity}
                />) : (
                item.storableValue)}
            </td>
            <td width="200px">{onEditOpen ? (
                    <>
                        <button
                            className={"button-left"}
                            onClick={() => updateItem()}
                        >save
                        </button>
                        <button
                            className={"button-right"}
                            onClick={() => setOnEditOpen(!onEditOpen)}
                        >cancel
                        </button>
                    </>) :
                <>
                    <button
                        className={"button-left"}
                        onClick={() => setOnEditOpen(!onEditOpen)}
                    >Edit
                    </button>
                    <button
                        className="button-right"
                        onClick={() => deleteItem(item.id)}
                    >delete
                    </button>
                </>
            }
            </td>
        </tr>
    );
}

export default ItemTableRow;
