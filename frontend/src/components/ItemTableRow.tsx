import React, {ChangeEvent, useState} from 'react';
import {ItemModel} from "../model/ItemModel";
import axios from "axios";
import EditIcon from '@mui/icons-material/Edit';
import {IconButton} from "@mui/material";
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import CancelPresentationIcon from '@mui/icons-material/CancelPresentation';
import SaveIcon from '@mui/icons-material/Save';
import {pink} from "@mui/material/colors";


type ItemTableRowProps = {
    item: ItemModel
    fetchItemData: () => void
    setItemTableRowErrorModal: (showItemErrorAlert: boolean) => void
    setItemTableRowErrorMessage: (itemErrorMessage: string) => void
    setItemTableRowSuccessMessage: (itemSuccessMessage: string) => void
    setItemTableRowSuccessModal: (showItemSuccessAlert: boolean) => void
}

function ItemTableRow({
                          item,
                          fetchItemData,
                          setItemTableRowSuccessModal,
                          setItemTableRowSuccessMessage,
                          setItemTableRowErrorMessage,
                          setItemTableRowErrorModal
                      }: ItemTableRowProps) {

    const [storableValue, setStorableValue] = useState<string>("");
    const [onEditOpen, setOnEditOpen] = useState(false);

    const updateItem = () => {
        axios.put("/api/items/" + item.id, {...item, storableValue})
            .then(response => {
                if (response.status === 200) {
                    setItemTableRowSuccessModal(true);
                    setItemTableRowSuccessMessage("item updated");
                }
            })
            .catch(error => {
                if (error.response) {
                    setItemTableRowErrorModal(true);
                    setItemTableRowErrorMessage(error.response.data)
                    ;
                }
            })
            .then(fetchItemData)
            .then(() => setOnEditOpen(!onEditOpen))
    }
    const deleteItem = (id: string) => {
        axios.delete("/api/items/" + id)
            .then(response => {
                if (response.status === 204) {
                    setItemTableRowSuccessModal(true);
                    setItemTableRowSuccessMessage("item deleted")
                }
            })
            .catch(error => {
                if (error.response) {
                    setItemTableRowErrorModal(true);
                    setItemTableRowErrorMessage(error.response.data)
                }
            })
            .then(fetchItemData)
    }
    const handleInputCapacity = (event: ChangeEvent<HTMLInputElement>) => {
        const validCapacity = event.target.value.replace(/\D/g, '')
        setStorableValue(validCapacity)
    }


    return (

        <tr key={item.id}>
            <td className="center">{item.itemNumber}</td>
            <td>{item.ean}</td>
            <td>{item.name}</td>
            <td>{item.categoryName}</td>
            <td className="center">{item.issuingCountry}</td>
            <td className="center">{onEditOpen ? (
                <input id="edit_input" value={storableValue}
                       onChange={handleInputCapacity}
                />) : (
                item.storableValue)}
            </td>
            <td className="center">{item.storageBinAmount}</td>
            <td className="td_actions" width="200px">{onEditOpen ? (
                    <>
                        <IconButton sx={{marginLeft: 4}} aria-label="save" size="small" onClick={() => updateItem()}>
                            <SaveIcon color="success" fontSize="inherit"/>
                        </IconButton>
                        <IconButton aria-label="cancel" size="small" onClick={() => setOnEditOpen(!onEditOpen)}>
                            <CancelPresentationIcon fontSize="inherit"/>
                        </IconButton>
                    </>) :
                <>
                    <IconButton sx={{marginLeft: 4}} aria-label="edit" size="small"
                                onClick={() => setOnEditOpen(!onEditOpen)}>
                        <EditIcon color="primary" fontSize="inherit"/>
                    </IconButton>
                    <IconButton aria-label="delete" size="small">
                        <DeleteForeverIcon sx={{color: pink[500]}} fontSize="inherit"
                                           onClick={() => deleteItem(item.id)}/>
                    </IconButton>
                </>
            }
            </td>
        </tr>
    );
}

export default ItemTableRow;
