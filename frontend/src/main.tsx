import '@ant-design/v5-patch-for-react-19';
import React from "react";
import App from "./App.tsx";
import {BrowserRouter} from "react-router-dom";
import {createRoot} from "react-dom/client";
import 'antd/dist/reset.css'
createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
            <BrowserRouter>
                <App/>
            </BrowserRouter>
    </React.StrictMode>
);