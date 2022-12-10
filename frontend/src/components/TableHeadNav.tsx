import React from 'react';
import {AppBar, Container, Link, Toolbar} from "@mui/material";
import Typography from "@mui/material/Typography";
import logoName from "../images/Logo_Name.png";
import "../css/TableNavigation.css"

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
                    <Typography variant="h6" color="inherit" sx={{marginTop: 2, display: "flex"}}>
                        <img id="logo-name-table-head-nav" src={logoName} alt={"logo_name"}/>
                        <Container sx={{marginLeft:10}}>
                            <Link href="/" underline="none" sx={{
                                marginLeft: 10, color: "grey",
                                '&:hover': {
                                    color: "#1976d2"
                                }
                            }}>
                                {"home"}
                            </Link>
                            <Link href="/item-page" underline="none" sx={{
                                marginLeft: 10, color: "grey",
                                '&:hover': {
                                    color: "#1976d2"
                                }
                            }}> {"items"}
                            </Link>
                            <Link href="/storage-page" underline="none" sx={{
                                marginLeft: 10, color: "grey",
                                '&:hover': {
                                    color: "#1976d2"
                                }
                            }}>
                                {"storage"}
                            </Link>
                            <Link href="/store-page" underline="none" sx={{
                                marginLeft: 10, color: "grey",
                                '&:hover': {
                                    color: "#1976d2"
                                }
                            }}>
                                {"storing"}
                            </Link>
                            <Link href="/retrieval-page" underline="none" sx={{
                                marginLeft: 10, color: "grey",
                                '&:hover': {
                                    color: "#1976d2"
                                }
                            }}>
                                {"retrieval"}
                            </Link>
                        </Container>
                    </Typography>
                </Toolbar>
            </AppBar>
        </div>
    );

}

export default TableHeadNav;
