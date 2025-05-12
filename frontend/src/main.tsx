// import '@ant-design/v5-patch-for-react-19';
import React from "react";
import {BrowserRouter} from "react-router-dom";
import {createRoot} from "react-dom/client";
// import 'antd/dist/reset.css'
import './index.css'
import App2 from "@/App2.tsx";

createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
            <BrowserRouter>
                {/* <App/> */}
                <App2/>
            </BrowserRouter>
    </React.StrictMode>
);