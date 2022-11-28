import React from 'react';
import MainPageItemCard from "./MainPageItemCard";
import MainPageNavigation from "./MainPageNavigation"
import "../css/MainPage.css";
import {NavLink} from "react-router-dom";


function MainPage() {
    return (


        <div>
            <MainPageNavigation/>
            <div className="main-page-container">
                <NavLink to={"/item-page"} style={{textDecoration: 'none'}}><MainPageItemCard/></NavLink>
            </div>
        </div>

    );
}

export default MainPage;