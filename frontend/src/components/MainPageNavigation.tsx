import React from 'react';
import {AppBar, Button, Toolbar} from "@mui/material";
import Typography from "@mui/material/Typography";

function MainPageNavigation() {
    return (
        <AppBar
            position="static"
            color="default"
            elevation={0}
            sx={{borderBottom: (theme) => `1px solid ${theme.palette.divider}`}}
        >
            <Toolbar>
                <Typography variant="h6" color="inherit" sx={{flexGrow: 1}}>
                    Storeify
                </Typography>
                <Button variant="outlined" sx={{my: 1, mx: 1.5}}>
                    application test
                </Button>
            </Toolbar>
        </AppBar>
    );
}

export default MainPageNavigation;
