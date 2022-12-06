import React, {ChangeEvent} from 'react';
import axios from "axios";

type retrievalNavigationProps = {
    fetchRetrievalData: () => void
    amountValue: string
    storageLocationId: string
    itemNumber: string
    setStorageLocationId: (storageBinNumber: string) => void
    setItemNumber: (itemNumber: string) => void
    setAmountValue: (amount: string) => void
    handleOutputAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleOutputItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleOutputStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
    setErrorModal: (showAlert: boolean) => void
    setErrorMessage: (errorMessage: string) => void
    setSuccessMessage: (successMessage: string) => void
    setSuccessModal: (showSuccessAlert: boolean) => void
}

function RetrievalNavigation(props: retrievalNavigationProps) {

    const handleSubmit = (event: ChangeEvent<HTMLFormElement>) => {
        event.preventDefault()
        const retrievalItemNumber = props.itemNumber
        const retrievalStorageLocationId = props.storageLocationId
        const retrievalAmount = props.amountValue

        axios.post("/api/driving-orders/?type=OUTPUT", {
            itemNumber: retrievalItemNumber,
            storageLocationId: retrievalStorageLocationId,
            amount: retrievalAmount
        })
            .then(response => {
                if (response.status === 201) {
                    props.setSuccessModal(true);
                    props.setSuccessMessage("new driving order created ")
                }
            })
            .catch(error => {
                if (error.response) {
                    props.setErrorModal(true);
                    props.setErrorMessage(error.response.data)
                }
            }).then(props.fetchRetrievalData)

    }
    return (
        <div className="topnav">
            <div id="navi-logo">retrieval</div>
            <div className="add-container">
                <form onSubmit={handleSubmit}>
                    <input className="item_input_field" value={props.itemNumber}
                           onChange={props.handleOutputItemNumber} type="text" placeholder="item-number"
                           name="item-number"/>
                    <input className="item_input_field" value={props.storageLocationId}
                           onChange={props.handleOutputStorageBinNumber} type="text" placeholder="storage-bin-nr"
                           name="storage-bin-nr"/>
                    <input className="item_input_field" value={props.amountValue}
                           onChange={props.handleOutputAmount} type="text" placeholder="amount" name="amount"/>
                    <button type="submit">add</button>
                </form>
            </div>
        </div>
    );
}

export default RetrievalNavigation;