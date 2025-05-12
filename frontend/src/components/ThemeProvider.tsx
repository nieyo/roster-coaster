import React, { useCallback, useEffect, useMemo, useState } from "react";
import { Theme, ThemeProviderContext } from "@/context/theme-context.ts";

type ThemeProviderProps = {
    children: React.ReactNode;
    defaultTheme?: Theme;
    storageKey?: string;
};

export function ThemeProvider({
                                  children,
                                  defaultTheme = "system",
                                  storageKey = "vite-ui-theme",
                                  ...props
                              }: Readonly<ThemeProviderProps>) {
    const [theme, setTheme] = useState<Theme>(
        () => (localStorage.getItem(storageKey) as Theme) ?? defaultTheme
    );

    useEffect(() => {
        const root = window.document.documentElement;
        root.classList.remove("light", "dark");

        if (theme === "system") {
            const systemTheme = window.matchMedia("(prefers-color-scheme: dark)").matches
                ? "dark"
                : "light";
            root.classList.add(systemTheme);
            return;
        }

        root.classList.add(theme);
    }, [theme]);

    const setThemeAndStore = useCallback(
        (theme: Theme) => {
            localStorage.setItem(storageKey, theme);
            setTheme(theme);
        },
        [setTheme, storageKey]
    );

    const value = useMemo(
        () => ({
            theme,
            setTheme: setThemeAndStore,
        }),
        [theme, setThemeAndStore]
    );

    return (
        <ThemeProviderContext.Provider {...props} value={value}>
            {children}
        </ThemeProviderContext.Provider>
    );
}
