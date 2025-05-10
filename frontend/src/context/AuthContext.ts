import { createContext } from "react";
import { UserDTO } from "@/context/UserDTO.ts";
import {LoginRequest} from "@/context/LoginRequest.ts";

export type AuthContextType = {
    user: UserDTO | null;
    isAuthenticated: boolean;
    login: (request: LoginRequest) => Promise<void>;
    logout: () => void;
};

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
