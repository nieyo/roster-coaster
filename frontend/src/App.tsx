import "@mantine/core/styles.css";
import '@mantine/dates/styles.css';

import {Route, Routes} from "react-router-dom";
import ShiftGallery from "./components/shift/ShiftGallery.tsx";
import Header from "./components/layout/Header.tsx";
import AddShiftForm from "./components/shift/AddShiftForm.tsx";
import useShiftState from "./hooks/useShiftState.ts";
import {Container, Space, Stack} from "@mantine/core";

export default function App() {

    const {
        shiftList,
        shiftListIsLoading,
        shiftListError,
        getShiftList,
        deleteShift
    } = useShiftState();

    if (shiftListIsLoading) return <div>Loading...</div>;
    if (shiftListError) return <div>Error: {shiftListError}</div>;

    return (
        <Container>
            <Stack>
                <Space/>
                <Header/>
                <Routes>
                    <Route path={"/"} element={
                        <ShiftGallery
                            shifts={shiftList}
                            handleDelete={deleteShift}
                        />
                    }/>
                    <Route path={"/add"} element={
                        <AddShiftForm handleUpdate={getShiftList}/>
                    }/>
                </Routes>
            </Stack>
        </Container>
    )
}