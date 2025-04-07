import {Layout, ConfigProvider, theme} from "antd";
import {Route, Routes} from "react-router-dom";
import ShiftGallery from "./components/ShiftGallery.tsx";
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

        <ConfigProvider
            theme={{
                algorithm: theme.darkAlgorithm,
            }}>
            <Layout style={{minHeight: '100vh'}}>
                <Layout.Content style={{padding: '48px 48px'}}>

                    <Routes>
                        <Route path={"/"} element={
                            <ShiftGallery
                                shifts={shiftList}
                                handleDelete={deleteShift}
                                handleUpdate={updateShift}
                                handleSave={saveShift}
                            />
                        }/>
                    </Routes>
                </Layout.Content>
            </Layout>
        </ConfigProvider>
    )
}