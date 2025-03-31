import "@mantine/core/styles.css";
import '@mantine/dates/styles.css';

import {Route, Routes} from "react-router-dom";
import ShiftGallery from "./components/shift/ShiftGallery.tsx";
import Header from "./components/layout/Header.tsx";
import AddShiftForm from "./components/shift/AddShiftForm.tsx";
import useShiftState from "./hooks/useShiftState.ts";

export default function App() {

    const {shiftList, shiftListIsLoading, shiftListError, getShiftList} = useShiftState();

    if (shiftListIsLoading) return <div>Loading...</div>;
    if (shiftListError) return <div>Error: {shiftListError}</div>;

    return (
        <>
            <Header/>
            <Routes>
                <Route path={"/"} element={<ShiftGallery shifts={shiftList}/>}/>
                <Route path={"/add"} element={<AddShiftForm handleUpdate={getShiftList}/>}/>
            </Routes>
        </>
    )
}