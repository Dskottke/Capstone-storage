import React, {ChangeEvent} from 'react';

type retrievalNavigationProps = {
    amountValue: string
    storageBinNumber: string
    itemNumber: string
    setStorageBinNumber: (storageBinNumber: string) => void
    setItemNumber: (itemNumber: string) => void
    setAmountValue: (amount: string) => void
    handleInputAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleInputItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleInputStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
}

function RetrievalNavigation(props: retrievalNavigationProps) {

    const handleAddSubmit = (event: ChangeEvent<HTMLFormElement>) => {
        event.preventDefault()
    }
    return (
        <div className="topnav">
            <div id="navi-logo">retrieval</div>
            <div className="add-container">
                <form onSubmit={handleAddSubmit}>
                    <input className="item_input_field" value={props.itemNumber}
                           onChange={props.handleInputItemNumber} type="text" placeholder="item-number"
                           name="item-number"/>
                    <input className="item_input_field" value={props.storageBinNumber}
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

export default RetrievalNavigation;