import React from 'react';
import {Card, CardContent} from "@mui/material";
import Typography from "@mui/material/Typography";
import "../css/MainPageItemCard.css"
import bodypart from '../images/Item_Icon_in_MaskGroup.png';

function ItemCard() {
    return (

        <Card sx={{marginLeft: 10, minWidth: 500, minHeight: 400, boxShadow: 5, borderRadius: 10 ,
            '&:hover': {
                boxShadow:14
            }}}>
            <div className={"item-card"}>
                <img className={"item-card-bodypart"} src={bodypart} alt={"item"}/>
            </div>
            <CardContent>
                <Typography variant="h5" component="div" align="center">
                    Items
                </Typography>
                <Typography variant="body2" color="text.secondary" align="center">
                    item overview
                </Typography>
            </CardContent>
        </Card>
    );
}

export default ItemCard;
