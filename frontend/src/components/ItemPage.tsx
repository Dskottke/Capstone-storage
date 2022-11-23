import React, {useEffect, useState} from 'react';
import axios from "axios";

import {ItemModel} from "../model/ItemModel";
import ItemNavigation from "./ItemNavigation";
import ItemTable from "./ItemTable";
import "../css/ItemPage.css"

function ItemPage() {
    const [data, setData] = useState<ItemModel[]>([]);

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axios.get("/api/items/")
            .then(response => {
                    return response.data
                }
            ).catch(error => {
            return error
        })
            .then((data) => setData(data))
    }
    return (
        <div className={"item-page-container"}>
            <ItemNavigation fetchData={fetchData}/>
            <div className={"item-page-body"}>
                <ItemTable fetchData={fetchData} data={data}/>
            </div>
        </div>
    );
}
export default ItemPage;
