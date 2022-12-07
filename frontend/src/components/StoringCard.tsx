import React from 'react';
import {Card, CardContent} from "@mui/material";
import Typography from "@mui/material/Typography";
import "../css/StoringCard.css"
import bodypart from '../images/Storing_in_Maskgroup.png';

function StoringCard() {
    return (
        <Card sx={{marginLeft: 10, minWidth: 500, minHeight: 400, boxShadow: 5, borderRadius: 10}}>
            <div className={"storing-order-input-card"}>
                <img className={"storing-order-card-bodypart"} src={bodypart} alt={"item"}/>
            </div>
            <CardContent>
                <Typography variant="h5" component="div" align="center">
                    store
                </Typography>
                <Typography variant="body2" color="text.secondary" align="center">
                    storing-order overview
                </Typography>
            </CardContent>
        </Card>
    );
}

export default StoringCard;
