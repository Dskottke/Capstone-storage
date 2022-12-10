import React from 'react';
import {Card, CardContent} from "@mui/material";
import Typography from "@mui/material/Typography";
import "../css/MainPageStorageCard.css"
import bodypart from '../images/StorageBin_in_MaskGroup.png';

function StorageCard() {
    return (
        <Card sx={{marginLeft: 10, minWidth: 345, minHeight: 400, boxShadow: 5, borderRadius: 10,
            '&:hover': {
                boxShadow:14
            }}}>
            <div className={"storage-card-header-red"}>
                <img className={"storage-bodypart"} src={bodypart} alt={"body-part"}/>
            </div>
            <CardContent>
                <Typography variant="h5" component="div" align="center">
                    storage-bins
                </Typography>
                <Typography variant="body2" color="text.secondary" align="center">
                    Storage overview
                </Typography>
            </CardContent>
        </Card>
    );
}

export default StorageCard;
