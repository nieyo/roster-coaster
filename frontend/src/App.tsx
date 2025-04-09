import {Layout, ConfigProvider, theme} from "antd";
import {Route, Routes} from "react-router-dom";
import ShiftGallery from "./components/ShiftGallery.tsx";
import useShiftState from "./hooks/useShiftState.ts";
import {useState} from "react";

export default function App() {
    const [isDarkMode, setIsDarkMode] = useState(true);
    const toggleTheme = () => {
        setIsDarkMode(prev => !prev);
    };

    const {
        shiftList,
        saveShift,
        updateShift,
        deleteShift
    } = useShiftState();


    return (

        <ConfigProvider
            theme={{
                algorithm: isDarkMode ? theme.darkAlgorithm : theme.defaultAlgorithm,
            }}>
            <Layout style={{minHeight: '100vh'}}>
                <Layout.Content
                    style={{
                        padding: '48px 24px',
                        maxWidth: '1200px',
                        width: '90%',
                        margin: '0 auto',
                        transition: 'all 0.3s ease',
                    }}
                >

                    <Routes>
                        <Route path={"/"} element={
                            <ShiftGallery
                                shifts={shiftList}
                                handleDelete={deleteShift}
                                handleUpdate={updateShift}
                                handleSave={saveShift}
                                toggleTheme={toggleTheme}
                                isDarkMode={isDarkMode}
                            />
                        }/>
                    </Routes>
                </Layout.Content>
            </Layout>
        </ConfigProvider>
    )
}