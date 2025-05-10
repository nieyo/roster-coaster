import {useState, useMemo, useCallback, ReactNode, useEffect} from "react";
import {AuthContext, AuthContextType} from "./AuthContext";
import {UserDTO} from "@/context/UserDTO.ts";
import {AuthResponse} from "@/context/AuthResponse.ts";
import {LoginRequest} from "@/context/LoginRequest.ts";
import {jwtDecode} from "jwt-decode";
import {JwtPayload} from "@/context/JwtPayload.ts";
import {useNavigate} from "react-router-dom";

const LOGIN_API_URL = "/api/auth/login";

export function AuthProvider({children}: Readonly<{ children: ReactNode }>) {
    const [user, setUser] = useState<UserDTO | null>(null);
    const navigate = useNavigate();


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
        setUser({name: data.name, email: data.email});
    }, []);

    const logout = useCallback(() => {
        setUser(null);
        localStorage.removeItem("token");
        navigate("/login");
    }, [navigate]);

    const isAuthenticated = !!user;

    const value = useMemo<AuthContextType>(
        () => ({user, isAuthenticated, login, logout}),
        [user, isAuthenticated, login, logout]
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
    }, []);

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
}
