import React from 'react';
import {AppBar, Container, Link, Toolbar} from "@mui/material";
import Typography from "@mui/material/Typography";
import logoName from "../images/Logo_Name.png";

function TableHeadNav() {
    return (
        <div>
            <AppBar
                position="static"
                color="default"
                elevation={0}
                sx={{borderBottom: (theme) => `1px solid ${theme.palette.divider}`}}
            >
                <Toolbar>
                    <Typography variant="h6" color="inherit" sx={{display: "flex"}}>
                        <img id={"logo-name"} src={logoName} alt={"logo_name"}/>
                        <Container sx={{marginLeft: 40}}>
                            <Link href="/" underline="none" sx={{marginLeft: 10, color: "grey"}}>
                                {"home"}
                            </Link>
                            <Link href="/item-page" underline="none" sx={{marginLeft: 10, color: "grey"}}>
                                {"items"}
                            </Link>
                            <Link href="/storage-page" underline="none" sx={{marginLeft: 10, color: "grey"}}>
                                {"storage"}
                            </Link>
                            <Link href="/store-page" underline="none" sx={{marginLeft: 10, color: "grey"}}>
                                {"storing"}
                            </Link>
                        </Container>
                    </Typography>
                </Toolbar>
            </AppBar>
        </div>
    );

}

export default TableHeadNav;
