import {Route, Routes} from "react-router-dom";
import {LoginForm} from "@/components/login-form"
import {ThemeProvider} from "./components/theme-provider";
import {RegisterForm} from "@/components/register-form.tsx";
import {ForgotPasswordForm} from "@/components/ForgotPasswordForm.tsx";
import ProtectedRoute from "@/components/ProtectedRoute.tsx";

export default function App2() {
    return (
        <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
            <Routes>

                <Route path={"/login"} element={
                    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
                        <div className="w-full max-w-sm">
                            <LoginForm/>
                        </div>
                    </div>
                }/>

                <Route path={"/register"} element={
                    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
                        <div className="w-full max-w-sm">
                            <RegisterForm/>
                        </div>
                    </div>
                }/>

                <Route path={"/recover"} element={
                    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
                        <div className="w-full max-w-sm">
                            <ForgotPasswordForm/>
                        </div>
                    </div>
                }/>

                <Route path="/home" element={
                    <ProtectedRoute>
                        <p>Test, should not be visible</p>
                    </ProtectedRoute>
                }/>

            </Routes>
        </ThemeProvider>
    )
}