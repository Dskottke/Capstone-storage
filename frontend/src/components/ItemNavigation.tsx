import React, {ChangeEvent, useState} from 'react';
import axios from "axios";
import "../css/ItemNavigation.css"

type itemNavigationProbs = {
    fetchData: () => void
    setErrorModal: (showErrorAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function ItemNavigation(props: itemNavigationProbs) {

    const [ean, setEan] = useState<string>()
    const [itemNumber, setItemNumber] = useState<string>()
    const [capacity, setCapacity] = useState<string>()

    const handleInputCapacity = (event: ChangeEvent<HTMLInputElement>) => {
        const validCapacity = event.target.value.replace(/\D/g, '')
        setCapacity(validCapacity)
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

        axios.post("/api/items/" + ean, {ean, itemNumber, capacity})
            .then(response => {
                if (response.status === 201) {
                    props.setSuccessModal(true);
                    props.setSuccessMessage("new item created ")
                }
            })
            .catch(error => {
                if (error.response.status === 400) {
                    props.setErrorModal(true);
                    props.setErrorMessage("item is already existing")
                }
                if (error.response.status === 500) {
                    props.setErrorModal(true);
                    props.setErrorMessage("found no matching item with ean")
                }
            })
            .then(props.fetchData)
            .then(() => setEan(""))
    }

    return (
        <div className="topnav">
            <div id="navi-logo">Items</div>
            <div className="add-container">

                <form onSubmit={handleAddSubmit}>
                    <input value={capacity}
                           onChange={handleInputCapacity} type="text" placeholder="capacity" name="submit"/>
                    <input value={itemNumber}
                           onChange={handleInputItemNumber} type="text" placeholder="item-number" name="submit"/>
                    <input value={ean}
                           onChange={handleInputEan} type="text" placeholder="ean" name="submit"/>
                    <button type="submit">add</button>
                </form>
            </div>
        </div>

    );
}

export default ItemNavigation;
