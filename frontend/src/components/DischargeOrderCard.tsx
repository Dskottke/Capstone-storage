import React from 'react';
import {Card, CardContent} from "@mui/material";
import bodypart from "../images/Discharge_in_Maskgroup.png";
import "../css/DischargeOrderCard.css"
import Typography from "@mui/material/Typography";

function DischargeOrderCard() {
    return (
        <Card sx={{marginLeft: 10, minWidth: 500, minHeight: 400, boxShadow: 5, borderRadius: 10}}>
            <div className={"discharge-order-card"}>
                <img className={"discharge-order-card-bodypart"} src={bodypart} alt={"item"}/>
            </div>
            <CardContent>
                <Typography variant="h5" component="div" align="center">
                    discharge
                </Typography>
                <Typography variant="body2" color="text.secondary" align="center">
                    discharge-order overview
                </Typography>
            </CardContent>
        </Card>
    );
}

export default DischargeOrderCard;