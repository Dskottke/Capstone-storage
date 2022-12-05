import React, {ChangeEvent} from 'react';
import axios from "axios";

type storingNavigationProps = {
    amountValue: string
    storageLocationNumber: string
    itemNumber: string
    setStorageBinNumber: (storageBinNumber: string) => void
    setItemNumber: (itemNumber: string) => void
    setAmountValue: (amount: string) => void
    handleInputAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleInputItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleInputStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
}

function StoringNavigation(props: storingNavigationProps) {

    const handleAddSubmit = (event: ChangeEvent<HTMLFormElement>) => {
        event.preventDefault()
        const itemNumber = props.itemNumber
        const storageLocationNumber = props.storageLocationNumber
        const amount = props.amountValue

        axios.post("/api/driving-orders/input", {itemNumber, storageLocationNumber, amount})
            .catch(error => console.error(error))

    }

    return (
        <div className="topnav">
            <div id="navi-logo">storing</div>
            <div className="add-container">
                <form onSubmit={handleAddSubmit}>
                    <input className="item_input_field" value={props.itemNumber}
                           onChange={props.handleInputItemNumber} type="text" placeholder="item-number"
                           name="item-number"/>
                    <input className="item_input_field" value={props.storageLocationNumber}
                           onChange={props.handleInputStorageBinNumber} type="text" placeholder="storage-bin-nr"
                           name="ean"/>
                    <input className="item_input_field" value={props.amountValue}
                           onChange={props.handleInputAmount} type="text" placeholder="amount" name="amount"/>
                    <button type="submit">add</button>
                </form>
            </div>
        </div>

    );
}

export default StoringNavigation;