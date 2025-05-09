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
import {Link} from "react-router-dom";
import {ModeToggle} from "@/components/mode-toggle.tsx";

export function RegisterForm({
                              className,
                              ...props
                          }: React.ComponentProps<"div">) {
    return (
        <div className={cn("flex flex-col gap-6", className)} {...props}>
            <div className="absolute top-4 right-4">
                <ModeToggle />
            </div>
            <Card>
                <CardHeader>
                    <CardTitle>Register a new Account</CardTitle>
                    <CardDescription>
                        Enter your email below to register your account
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form>
                        <div className="flex flex-col gap-6">
                            <div className="grid gap-3">
                                <Label htmlFor="email">Email</Label>
                                <Input id="email" name="email" type="email" placeholder="m@example.com" required />
                            </div>
                            <div className="grid gap-3">
                                <Label htmlFor="password">Password</Label>
                                <Input id="password" name="password" type="password" required />
                            </div>
                            <div className="grid gap-3">
                                <Label htmlFor="passwordConfirmation">Confirm Password</Label>
                                <Input id="passwordConfirmation" name="passwordConfirmation" type="password" required />
                            </div>
                            <Button type="submit" className="w-full">Register</Button>
                        </div>
                        <div className="mt-4 text-center text-sm">
                            Already have an account?{" "}
                            <Link to="/login" className="underline underline-offset-4">Login</Link>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}
