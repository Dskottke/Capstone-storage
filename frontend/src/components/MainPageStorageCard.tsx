import React from 'react';
import {Card, CardContent} from "@mui/material";
import Typography from "@mui/material/Typography";
import "../css/MainPageStorageCard.css"
import storage from "../images/storage-logo.png";

function MainPageStorageCard() {
    return (
        <Card sx={{marginLeft: 10, minWidth: 345, minHeight: 400, boxShadow: 5}}>
            <div className={"storage-card-header-red"}>
                <img className={"storage-logo"} width={"30px"} src={storage} alt={"storage-logo"}/>
            </div>
            <CardContent>
                <Typography variant="h5" component="div">
                    storage-bins
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    Storage overview
                </Typography>
            </CardContent>
        </Card>
    );
}

export default MainPageStorageCard;
