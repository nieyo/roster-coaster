import {cn} from "@/lib/utils.ts"
import {Button} from "@/components/ui/button.tsx"
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card.tsx"
import {Input} from "@/components/ui/input.tsx"
import {Label} from "@/components/ui/label.tsx"
import {Link, Navigate, useNavigate} from "react-router-dom";
import {ModeToggle} from "@/components/ModeToggle.tsx";
import React, {useState} from "react";
import {useAuth} from "@/context/useAuth"

export function RegisterForm({className, ...props}: React.ComponentProps<"div">) {

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [passwordConfirmation, setPasswordConfirmation] = useState("");
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const {register, isAuthenticated} = useAuth();
    const navigate = useNavigate();

    if (isAuthenticated) {
        return <Navigate to="/home" replace/>;
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);

        if (password !== passwordConfirmation) {
            setError("Passwords do not match.");
            return;
        }

        setLoading(true);
        try {
            await register({name, email, password});
            navigate("/home");
        } catch (e) {
            setError(e instanceof Error ? e.message : "Registration failed.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={cn("flex flex-col gap-6", className)} {...props}>
            <Card>
                <CardHeader>
                    <div className="flex items-start justify-between">
                        <div>
                            <CardTitle className="mb-2">Register a new Account</CardTitle>
                            <CardDescription>
                                Enter your email below to register your account
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
                                <Label htmlFor="name">Name</Label>
                                <Input
                                    id="name"
                                    name="name"
                                    type="name"
                                    placeholder="Name"
                                    required
                                    autoComplete="name"
                                    value={name}
                                    onChange={e => {
                                        setName(e.target.value)
                                        setError(null);
                                    }}
                                />
                            </div>
                            <div className="grid gap-3">
                                <Label htmlFor="email">Email</Label>
                                <Input
                                    id="email"
                                    name="email"
                                    type="email"
                                    placeholder="m@example.com"
                                    required
                                    autoComplete="email"
                                    value={email}
                                    onChange={e => {
                                        setEmail(e.target.value)
                                        setError(null);
                                    }}
                                />
                            </div>
                            <div className="grid gap-3">
                                <Label htmlFor="password">Password</Label>
                                <Input
                                    id="password"
                                    name="password"
                                    type="password"
                                    required
                                    autoComplete="new-password"
                                    value={password}
                                    onChange={e => {
                                        setPassword(e.target.value)
                                        setError(null);
                                    }}
                                />
                            </div>
                            <div className="grid gap-3">
                                <Label htmlFor="passwordConfirmation">Confirm Password</Label>
                                <Input
                                    id="passwordConfirmation"
                                    name="passwordConfirmation"
                                    type="password"
                                    required
                                    autoComplete="new-password"
                                    value={passwordConfirmation}
                                    onChange={e => {
                                        setPasswordConfirmation(e.target.value)
                                        setError(null);
                                    }}
                                />
                            </div>
                            <div className="flex flex-col gap-3">
                                <Button
                                    type="submit"
                                    className={cn("w-full", error ? "text-red-500" : "")}
                                    disabled={loading}
                                >
                                    {loading
                                        ? "Registering..."
                                        : error ?? "Register"}
                                </Button>
                            </div>

                            <div className="mt-4 text-center text-sm">
                                Already have an account?{" "}
                                <Link to="/login" className="underline underline-offset-4">Login</Link>
                            </div>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
)
}
