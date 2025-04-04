import {Route, Routes} from "react-router-dom";
import ShiftGallery from "./components/shift/ShiftGallery.tsx";
import Header from "./components/layout/Header.tsx";
import ShiftForm from "./components/shift/ShiftForm.tsx";
import useShiftState from "./hooks/useShiftState.ts";

export default function App() {

    const {
        shiftList,
        shiftListError,
        saveShift,
        updateShift,
        deleteShift
    } = useShiftState();

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
                            handleUpdate={updateShift}
                        />
                    }/>

                    <Route path={"/add"} element={
                        <ShiftForm
                            handleSave={saveShift}
                            handleUpdate={() => {}}
                        />
                    }/>

                    <Route path={"/edit/:id"} element={
                        <ShiftForm
                            handleSave={() => {}}
                            handleUpdate={updateShift}
                        />
                    }/>
                </Routes>
            </Stack>
        </Container>
    )
}