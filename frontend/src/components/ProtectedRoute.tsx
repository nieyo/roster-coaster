import { ReactNode } from "react";
import { Navigate } from "react-router-dom";

type ProtectedRouteProps = {
    children: ReactNode;
};

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
    const isAuthenticated = !!localStorage.getItem("token"); // Replace with your auth logic
    return isAuthenticated ? children : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
