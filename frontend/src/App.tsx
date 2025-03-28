import "@mantine/core/styles.css";
import '@mantine/dates/styles.css';

import {Route, Routes} from "react-router-dom";
import ShiftGallery from "./components/shift/ShiftGallery.tsx";
import Header from "./components/layout/Header.tsx";
import AddShiftForm from "./components/shift/AddShiftForm.tsx";


export default function App() {


    return (
        <>
            <Header/>
            <Routes>
                <Route path={"/"} element={<ShiftGallery/>}/>
                <Route path={"/add"} element={<AddShiftForm/>}/>
            </Routes>
        </>
    )
}