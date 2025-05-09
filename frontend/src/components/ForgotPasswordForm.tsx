import {Card, CardHeader, CardTitle, CardDescription, CardContent} from "@/components/ui/card"
import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Link} from "react-router-dom";
import {cn} from "@/lib/utils.ts";
import {ModeToggle} from "@/components/mode-toggle.tsx";

export function ForgotPasswordForm({
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
                    <CardTitle>Recover Password</CardTitle>
                    <CardDescription>
                        Enter your email address to reset your password.
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <form>
                        <div className="flex flex-col gap-6">
                            <div className="grid gap-3">
                                <Label htmlFor="email">Email</Label>
                                <Input
                                    id="email"
                                    type="email"
                                    placeholder="m@example.com"
                                    required
                                />
                            </div>

                            <div className="flex flex-col gap-3">
                                <Button type="submit" className="w-full">
                                    Send Reset Link
                                </Button>
                            </div>
                        </div>
                        <div className="mt-4 text-center text-sm">
                            <Link to="/login" className="underline underline-offset-4">
                                Back to Login
                            </Link>
                        </div>
                    </form>
                </CardContent>
            </Card>
        </div>
    )
}
