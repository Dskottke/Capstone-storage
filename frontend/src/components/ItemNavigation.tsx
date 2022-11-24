import React, {ChangeEvent, useState} from 'react';
import axios from "axios";
import "../css/ItemNavigation.css"


type itemNavigationProbs = {
    fetchData: () => void
    setFailModal: (showErrorAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function ItemNavigation(props: itemNavigationProbs) {
    const [ean, setEan] = useState<string>()

    const handleInputEan = (event: ChangeEvent<HTMLInputElement>) => {
        const validEan = event.target.value.replace(/\D/g, '')
        setEan(validEan)
    }
    const handleAddSubmit = (event: ChangeEvent<HTMLFormElement>) => {
        event.preventDefault()

        axios.post("/api/items/" + ean)
            .catch(error => {
                if (error.response.status === 500) {
                    props.setFailModal(true);
                    props.setErrorMessage("ean not found")
                }
            })
            .then(() => {
                props.setSuccessModal(true);
                props.setSuccessMessage("new item created ")
            })
            .then(props.fetchData)
            .then(() => setEan(""))
    }

    return (
        <div className="topnav">
            <div id="navi-logo">Items</div>
            <div className="add-container">

                <form onSubmit={handleAddSubmit}>
                    <input value={ean}
                           onChange={handleInputEan} type="text" placeholder="ean" name="submit"/>
                    <button type="submit">add</button>

                </form>
            </div>
        </div>

    );
}

export default ItemNavigation;
