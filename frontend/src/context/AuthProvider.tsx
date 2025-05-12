import {useState, useMemo, useCallback, ReactNode, useEffect} from "react";
import {AuthContext, AuthContextType} from "./AuthContext";
import {UserDTO} from "@/context/UserDTO.ts";
import {AuthResponse} from "@/context/AuthResponse.ts";
import {LoginRequest} from "@/context/LoginRequest.ts";
import {jwtDecode} from "jwt-decode";
import {JwtPayload} from "@/context/JwtPayload.ts";
import {useNavigate} from "react-router-dom";
import {RegisterRequest} from "@/context/RegisterRequest.ts";

const LOGIN_API_URL = "/api/auth/login";
const REGISTER_API_URL = "/api/auth/register";

export function AuthProvider({children}: Readonly<{ children: ReactNode }>) {
    const [user, setUser] = useState<UserDTO | null>(null);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);

    const register = useCallback(async (request: RegisterRequest) => {
        const response = await fetch(REGISTER_API_URL, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(request),
        });

        if (!response.ok) {
            throw new Error("Registration failed");
        }

        const data: AuthResponse = await response.json();
        localStorage.setItem("token", data.token);
        const payload = jwtDecode<JwtPayload>(data.token);
        setUser({ name: payload.name, email: payload.sub });
    }, []);

    const login = useCallback(async (request: LoginRequest) => {
        const response = await fetch(LOGIN_API_URL, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(request),
        });

        if (!response.ok) {
            throw new Error("Login failed");
        }

        const data: AuthResponse = await response.json();
        localStorage.setItem("token", data.token);
        const payload = jwtDecode<JwtPayload>(data.token);
        setUser({ name: payload.name, email: payload.sub });
    }, []);

    const logout = useCallback(() => {
        setUser(null);
        localStorage.removeItem("token");
        navigate("/login");
    }, [navigate]);

    const isAuthenticated = !!user;

    const value = useMemo<AuthContextType>(
        () => ({user, register, login, isAuthenticated, logout, loading}),
        [user, register, login, isAuthenticated, logout, loading]
    );

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const payload = jwtDecode<JwtPayload>(token);
                setUser({ name: payload.name, email: payload.sub });
            } catch (error) {
                console.error("Failed to decode JWT:", error);
                setUser(null);
                localStorage.removeItem("token");
            }
        }
        setLoading(false);
    }, []);

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}
