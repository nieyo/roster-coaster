import { useAuth } from "../context/useAuth";
import {Button} from "@/components/ui/button"

export default function TestComponent() {
    const { isAuthenticated, user, logout, loading } = useAuth();

    if (loading) {
        return (
            <div className="flex items-center justify-center min-h-32">
                <span>Loading...</span>
            </div>
        );
    }

    return (
        <div>
            <p>{user?.name} is {isAuthenticated ? "logged in" : "not logged in"} with email {user?.email}</p>
            <Button onClick={logout}>Logout</Button>
        </div>
    );
}
