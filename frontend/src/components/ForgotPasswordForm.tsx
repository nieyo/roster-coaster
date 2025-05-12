import {Card, CardHeader, CardTitle, CardDescription, CardContent} from "@/components/ui/card"
import {Button} from "@/components/ui/button"
import {Input} from "@/components/ui/input"
import {Label} from "@/components/ui/label"
import {Link} from "react-router-dom";
import {cn} from "@/lib/utils.ts";
import {ModeToggle} from "@/components/ModeToggle.tsx";
import React from "react";

export function ForgotPasswordForm({
                                       className,
                                       ...props
                                   }: React.ComponentProps<"div">) {

    return (
        <div className={cn("flex flex-col gap-6", className)} {...props}>
            <Card>
                <CardHeader>
                    <div className="flex items-start justify-between">
                        <div>
                            <CardTitle className="mb-2">Recover Password</CardTitle>
                            <CardDescription>
                                Enter your email address to reset your password
                            </CardDescription>
                        </div>
                        <div className={"pl-4"}>
                            <ModeToggle/>
                        </div>
                    </div>
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
