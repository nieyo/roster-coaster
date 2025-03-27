import React from "react";
import App from "./App.tsx";
import {BrowserRouter} from "react-router-dom";
import {MantineProvider} from "@mantine/core";
import {theme} from "./theme.ts";
import {createRoot} from "react-dom/client";

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <MantineProvider defaultColorScheme="auto" theme={theme}>
            <BrowserRouter>
                <App/>
            </BrowserRouter>
        </MantineProvider>
    </React.StrictMode>
);