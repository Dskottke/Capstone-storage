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

    const [ean, setEan] = useState<string>("")
    const [itemNumber, setItemNumber] = useState<string>("")
    const [storableValue, setStorableValue] = useState<string>("")

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
                    props.setSuccessModal(true);
                    props.setSuccessMessage("new item created ")
                }
            })
            .catch(error => {
                    if (error.response) {
                        props.setErrorModal(true);
                        props.setErrorMessage(error.response.data)
                    }
                }
            )
            .then(props.fetchData)
            .then(() => setEan(""))
            .then(() => setItemNumber(""))
            .then(() => setStorableValue(""))
    }


    return (
        <div className="topnav">
            <div id="navi-logo">Items</div>
            <a className="active" href="/">home</a>
            <div className="add-container">

                <form onSubmit={handleAddSubmit}>
                    <input value={itemNumber}
                           onChange={handleInputItemNumber} type="text" placeholder="item-number" name="item-number"/>
                    <input value={ean}
                           onChange={handleInputEan} type="text" placeholder="ean" name="ean"/>
                    <input value={storableValue}
                           onChange={handleInputCapacity} type="text" placeholder="capacity" name="capacity"/>
                    <button type="submit">add</button>
                </form>
            </div>
        </div>

    );
}

export default ItemNavigation;
