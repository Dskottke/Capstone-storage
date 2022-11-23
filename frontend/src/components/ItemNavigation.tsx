import React, {ChangeEvent, useState} from 'react';
import axios from "axios";
import "../css/ItemNavigation.css"

type menuPagePobs = {
    fetchData: () => void
}

function ItemNavigation(props: menuPagePobs) {
    const [ean, setEan] = useState<string>()

    const handleInputEan = (event: ChangeEvent<HTMLInputElement>) => {
        setEan(event.target.value)
    }
    const handleAddSubmit = (event: ChangeEvent<HTMLFormElement>) => {
        event.preventDefault()
        axios.post("/api/items/" + ean)
            .catch((error) => console.log("POST Error: " + error))
            .then(props.fetchData)
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
