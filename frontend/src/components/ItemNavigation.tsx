import React, {ChangeEvent, useState} from 'react';
import axios from "axios";
import "../css/ItemNavigation.css"
import Alert from '@mui/material/Alert';


type itemNavigationProbs = {
    fetchData: () => void
}

function ItemNavigation(props: itemNavigationProbs) {
    const [ean, setEan] = useState<string>()
    const [failModal, setFailModal] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")

    const handleInputEan = (event: ChangeEvent<HTMLInputElement>) => {
        const validEan = event.target.value.replace(/\D/g, '')
        setEan(validEan)
    }
    const handleAddSubmit = (event: ChangeEvent<HTMLFormElement>) => {
        event.preventDefault()


        axios.post("/api/items/" + ean)
            .catch(error => {
                if (error.response.status === 500) {
                    setFailModal(true);
                    setErrorMessage("ean not found")

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
                    <input value={ean}
                           onChange={handleInputEan} type="text" placeholder="ean" name="submit"/>
                    <button type="submit">add</button>

                </form>
            </div>
            {failModal &&
                <Alert severity="error" onClose={() => {
                    setFailModal(false)
                }}>{errorMessage}</Alert>
            }
        </div>

    );
}

export default ItemNavigation;
