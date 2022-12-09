import React, {ChangeEvent, useState} from 'react';
import axios from "axios";
import "../css/TableNavigation.css"
import AddCircleOutlineOutlinedIcon from '@mui/icons-material/AddCircleOutlineOutlined';

type itemNavigationProps = {

    fetchItemData: () => void
    setItemNavigationErrorModal: (showItemErrorAlert: boolean) => void
    setItemNavigationErrorMessage: (itemErrorMessage: string) => void
    setItemNavigationSuccessMessage: (itemSuccessMessage: string) => void
    setItemNavigationSuccessModal: (showItemSuccessAlert: boolean) => void
}

function ItemNavigation(props: itemNavigationProps) {

    const [ean, setEan] = useState("")
    const [itemNumber, setItemNumber] = useState("")
    const [storableValue, setStorableValue] = useState("")

    const handleInputCapacity = (event: ChangeEvent<HTMLInputElement>) => {
        const validCapacity = event.target.value.replace(/\D/g, '')
        setStorableValue(validCapacity)
    }

    const handleInputItemNumber = (event: ChangeEvent<HTMLInputElement>) => {
        const validItemNumber = event.target.value.replace(/\D/g, '')
        setItemNumber(validItemNumber)
    }

    const handleInputEan = (event: ChangeEvent<HTMLInputElement>) => {
        const validEan = event.target.value.replace(/\D/g, '')
        setEan(validEan)
    }

    const handleAddSubmit = (event: ChangeEvent<HTMLFormElement>) => {
        event.preventDefault()


        axios.post("/api/items/" + ean, {ean, itemNumber, storableValue})
            .then(response => {
                if (response.status === 201) {
                    props.setItemNavigationSuccessModal(true);
                    props.setItemNavigationSuccessMessage("new item created ")
                }
            })
            .catch(error => {
                    if (error.response) {
                        props.setItemNavigationErrorModal(true);
                        props.setItemNavigationErrorMessage(error.response.data)
                    }
                }
            )
            .then(props.fetchItemData)
            .then(() => setEan(""))
            .then(() => setItemNumber(""))
            .then(() => setStorableValue(""))
    }


    return (
        <div className="topnav">
            <div id="navi-logo">Items</div>
            <div className="add-container">
                <form onSubmit={handleAddSubmit}>
                    <input className="item_input_field" value={itemNumber}
                           onChange={handleInputItemNumber} type="text" placeholder="item-number" name="item-number"/>
                    <input className="item_input_field" value={ean}
                           onChange={handleInputEan} type="text" placeholder="ean" name="ean"/>
                    <input className="item_input_field" value={storableValue}
                           onChange={handleInputCapacity} type="text" placeholder="capacity" name="capacity"/>
                    <button type="submit"><AddCircleOutlineOutlinedIcon/></button>
                </form>
            </div>
        </div>

    );
}

export default ItemNavigation;
