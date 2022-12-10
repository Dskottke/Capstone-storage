import React from 'react';
import ItemCard from "./ItemCard";
import MainPageNavigation from "./MainPageNavigation"
import "../css/MainPage.css";
import {NavLink} from "react-router-dom";
import StorageCard from "./StorageCard";
import StoringCard from "./StoringCard";
import RetrievalCard from "./RetrievalCard";


function MainPage() {
    return (


        <div>
            <MainPageNavigation/>
            <nav className={"main-page-container"}>
                <NavLink to={"/item-page"} style={{textDecoration: 'none', marginTop: 40}}><ItemCard/></NavLink>
                <NavLink to={"/storage-page"}
                         style={{textDecoration: 'none', marginTop: 40}}><StorageCard/></NavLink>
                <NavLink to={"/store-page"}
                         style={{textDecoration: 'none', marginTop: 40}}><StoringCard/></NavLink>
                <NavLink to={"/retrieval-page"}
                         style={{textDecoration: 'none', marginTop: 40}}><RetrievalCard/></NavLink>
            </nav>
        </div>

    );
}

export default MainPage;
