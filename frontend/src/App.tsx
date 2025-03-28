import "@mantine/core/styles.css";
import '@mantine/dates/styles.css';

import {Route, Routes} from "react-router-dom";
import ShiftGallery from "./components/shift/ShiftGallery.tsx";
import Header from "./components/layout/Header.tsx";
import AddShiftForm from "./components/shift/AddShiftForm.tsx";
import useShiftState from "./hooks/useShiftState.ts";

export default function App() {

    const {shifts, shiftsIsLoading, shiftsError, getShifts} = useShiftState();
    if (shiftsIsLoading) return <div>Loading...</div>;
    if (shiftsError) return <div>Error: {shiftsError}</div>;

    return (
        <>
            <Header/>
            <Routes>
                <Route path={"/"} element={<ShiftGallery shifts={shifts}/>}/>
                <Route path={"/add"} element={<AddShiftForm handleUpdate={getShifts}/>}/>
            </Routes>
        </>
    )
}