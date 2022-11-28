import React from 'react';
import {Card, CardContent} from "@mui/material";
import Typography from "@mui/material/Typography";
import "../css/MainPageItemCard.css"
import item from '../images/item-logo.png'


function MainPageItemCard() {
    return (

        <Card sx={{maxWidth: 345, minHeight: 300, boxShadow: 5}}>
                <div className={"item-card-header-blue"}>
                    <img width={"40px"} src={item} alt={"item"}/>
                </div>
                <CardContent>
                    <Typography variant="h5" component="div">
                        Items
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        item overview
                    </Typography>
                </CardContent>
        </Card>
    );
}

export default MainPageItemCard;
