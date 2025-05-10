import {cn} from "@/lib/utils"
import {Button} from "@/components/ui/button"
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Link, Navigate, useNavigate} from "react-router-dom";
import {ModeToggle} from "@/components/ModeToggle.tsx";
import React, {useState} from "react";
import {useAuth} from "@/context/useAuth.ts";

export function LoginForm({className, ...props}: React.ComponentProps<"div">) {

    const {login, isAuthenticated} = useAuth();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    if (isAuthenticated) {
        return <Navigate to="/home" replace/>;
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        try {
            await login({email, password});
            navigate("/home");
        } catch (e) {
            console.error("Login error:", e);
            setError("Invalid email or password.");
        }
    };

    return (
        <div className={cn("flex flex-col gap-6", className)} {...props}>

            <Card>
                <CardHeader>
                    <div className="flex items-start justify-between">
                        <div>
                            <CardTitle className="mb-2">Login to your account</CardTitle>
                            <CardDescription>
                                Enter your email below to login to your account
                            </CardDescription>
                        </div>
                        <div className={"pl-4"}>
                            <ModeToggle/>
                        </div>
                    </div>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit}>
                        <div className="flex flex-col gap-6">
                            <div className="grid gap-3">
                                <Label htmlFor="email">Email</Label>
                                <Input
                                    id="email"
                                    type="email"
                                    autoComplete="email"
                                    placeholder="m@example.com"
                                    required
                                    value={email}
                                    onChange={e => {
                                        setEmail(e.target.value);
                                        setError(null);
                                    }}
                                />
                            </div>
                            <div className="grid gap-3">
                                <div className="flex items-center">
                                    <Label htmlFor="password">Password</Label>
                                    <Link to="/recover"
                                          className="ml-auto inline-block text-sm underline-offset-4 hover:underline">
                                        Forgot your password?
                                    </Link>
                                </div>
                                <Input
                                    id="password"
                                    type="password"
                                    autoComplete="current-password"
                                    required
                                    value={password}
                                    onChange={e => {
                                        setPassword(e.target.value);
                                        setError(null);
                                    }}
                                />
                            </div>
                            <div className="flex flex-col gap-3">
                                <Button type="submit" className={cn("w-full", error ? "text-red-500" : "")}>
                                    {error ?? "Login"}
                                </Button>
                                <Button type="button" variant={"destructive"} onClick={() => {
                                    setEmail("test@rc.local");
                                    setPassword("Start123");
                                    setError(null);
                                }}
                                >
                                    Demo Login
                                </Button>
                            </div>
                        </div>
                        <div className="mt-4 text-center text-sm">
                            Don&apos;t have an account?{" "}
                            <Link to="/register" className="underline underline-offset-4">
                                Sign up
                            </Link>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}
