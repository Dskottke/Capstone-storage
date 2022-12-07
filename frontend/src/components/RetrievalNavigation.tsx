import React, {ChangeEvent} from 'react';
import axios from "axios";

type retrievalNavigationProps = {
    fetchRetrievalData: () => void
    retrievalAmountValue: string
    retrievalStorageLocationId: string
    retrievalItemNumber: string
    setRetrievalStorageLocationId: (retrievalStorageBinNumber: string) => void
    setRetrievalItemNumber: (retrievalItemNumber: string) => void
    setRetrievalAmountValue: (retrievalAmount: string) => void
    handleRetrievalAmount: (event: ChangeEvent<HTMLInputElement>) => void
    handleRetrievalItemNumber: (event: ChangeEvent<HTMLInputElement>) => void
    handleRetrievalStorageBinNumber: (event: ChangeEvent<HTMLInputElement>) => void
    setRetrievalNavigationErrorModal: (showRetrievalAlert: boolean) => void
    setRetrievalNavigationErrorMessage: (retrievalErrorMessage: string) => void
    setRetrievalNavigationSuccessMessage: (retrievalSuccessMessage: string) => void
    setRetrievalNavigationSuccessModal: (showRetrievalSuccessAlert: boolean) => void
}

function RetrievalNavigation(props: retrievalNavigationProps) {

    const handleSubmit = (event: ChangeEvent<HTMLFormElement>) => {
        event.preventDefault()
        const retrievalItemNumber = props.retrievalItemNumber
        const retrievalStorageLocationId = props.retrievalStorageLocationId
        const retrievalAmount = props.retrievalAmountValue

        axios.post("/api/driving-orders/?type=OUTPUT", {
            itemNumber: retrievalItemNumber,
            storageLocationId: retrievalStorageLocationId,
            amount: retrievalAmount
        })
            .then(response => {
                if (response.status === 201) {
                    props.setRetrievalNavigationSuccessModal(true);
                    props.setRetrievalNavigationSuccessMessage("new driving order created ")
                }
            })
            .catch(error => {
                if (error.response) {
                    props.setRetrievalNavigationErrorModal(true);
                    props.setRetrievalNavigationErrorMessage(error.response.data)
                }
            }).then(props.fetchRetrievalData)

    }
    return (
        <div className="topnav">
            <div id="navi-logo">retrieval</div>
            <div className="add-container">
                <form onSubmit={handleSubmit}>
                    <input className="item_input_field" value={props.retrievalItemNumber}
                           onChange={props.handleRetrievalItemNumber} type="text" placeholder="item-number"
                           name="item-number"/>
                    <input className="item_input_field" value={props.retrievalStorageLocationId}
                           onChange={props.handleRetrievalStorageBinNumber} type="text" placeholder="storage-bin-nr"
                           name="storage-bin-nr"/>
                    <input className="item_input_field" value={props.retrievalAmountValue}
                           onChange={props.handleRetrievalAmount} type="text" placeholder="amount" name="amount"/>
                    <button type="submit">add</button>
                </form>
            </div>
        </div>
    );
}

export default RetrievalNavigation;
