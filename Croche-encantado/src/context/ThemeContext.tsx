"use client";

import React, { createContext, useContext, useEffect, useState } from "react";
import { db } from "@/lib/firebase";
import { doc, onSnapshot, setDoc } from "firebase/firestore";

type Theme = {
  name: string;
  background: string;
  foreground: string;
  primary: string;
  accent: string;
  card: string;
  border: string;
};

export const themes: Record<string, Theme> = {
  rose: {
    name: "Rosa Pastel",
    background: "#fdf8f5",
    foreground: "#4a4a4a",
    primary: "#fce7f3",
    accent: "#fda4af",
    card: "rgba(255, 255, 255, 0.8)",
    border: "#ffe4e6",
  },
  mint: {
    name: "Verde Menta",
    background: "#f0fdf4",
    foreground: "#166534",
    primary: "#dcfce7",
    accent: "#4ade80",
    card: "rgba(255, 255, 255, 0.8)",
    border: "#bbf7d0",
  },
  sky: {
    name: "Azul Céu",
    background: "#f0f9ff",
    foreground: "#075985",
    primary: "#e0f2fe",
    accent: "#38bdf8",
    card: "rgba(255, 255, 255, 0.8)",
    border: "#bae6fd",
  },
  lavender: {
    name: "Lavanda",
    background: "#faf5ff",
    foreground: "#6b21a8",
    primary: "#f3e8ff",
    accent: "#c084fc",
    card: "rgba(255, 255, 255, 0.8)",
    border: "#e9d5ff",
  },
  peach: {
    name: "Pêssego",
    background: "#fff7ed",
    foreground: "#9a3412",
    primary: "#ffedd5",
    accent: "#fb923c",
    card: "rgba(255, 255, 255, 0.8)",
    border: "#fed7aa",
  },
  sand: {
    name: "Areia",
    background: "#fdfcfb",
    foreground: "#451a03",
    primary: "#fef3c7",
    accent: "#d97706",
    card: "rgba(255, 255, 255, 0.8)",
    border: "#fde68a",
  },
  dark: {
    name: "Noturno Rosa",
    background: "#000000",
    foreground: "#ffe4e6", // Rosa bem clarinho para o texto
    primary: "#1a0a0c",    // Preto com leve toque de vinho
    accent: "#fb7185",     // Rosa vibrante para destaques
    card: "rgba(20, 10, 12, 0.9)",
    border: "#4c0519",     // Borda em tom de vinho escuro
  },
  midnight: {
    name: "Azul Meia-Noite",
    background: "#020617",
    foreground: "#f8fafc",
    primary: "#0f172a",
    accent: "#60a5fa",
    card: "rgba(15, 23, 42, 0.9)",
    border: "#1e293b",
  },
  forest: {
    name: "Verde Floresta",
    background: "#022c22",
    foreground: "#f0fdf4",
    primary: "#064e3b",
    accent: "#34d399",
    card: "rgba(6, 78, 59, 0.9)",
    border: "#065f46",
  },
  vintage: {
    name: "Creme Vintage",
    background: "#fdf5e6",
    foreground: "#5d4037",
    primary: "#f5f5dc",
    accent: "#a1887f",
    card: "rgba(255, 255, 255, 0.8)",
    border: "#d7ccc8",
  },
};

const ThemeContext = createContext<{
  currentTheme: string;
  setGlobalTheme: (themeKey: string) => Promise<void>;
  setLocalTheme: (themeKey: string) => void;
}>({
  currentTheme: "rose",
  setGlobalTheme: async () => {},
  setLocalTheme: () => {},
});

export function ThemeProvider({ children }: { children: React.ReactNode }) {
  const [currentTheme, setCurrentTheme] = useState("rose");

  useEffect(() => {
    // Listen to global theme setting in Firestore
    const unsubscribe = onSnapshot(doc(db, "settings", "theme"), (doc) => {
      if (doc.exists()) {
        const themeKey = doc.data().currentTheme;
        if (themes[themeKey]) {
          setCurrentTheme(themeKey);
          applyTheme(themeKey);
        }
      } else {
        applyTheme("rose");
      }
    });

    return () => unsubscribe();
  }, []);

  const applyTheme = (themeKey: string) => {
    const theme = themes[themeKey];
    const root = document.documentElement;
    const isDark = themeKey.includes("dark") || themeKey.includes("midnight") || themeKey.includes("forest");
    
    // Cores de Base
    root.style.setProperty("--background", theme.background);
    root.style.setProperty("--foreground", theme.foreground);
    root.style.setProperty("--card", theme.card);
    
    // Escala de Cores do Tema (Mapeando a marca "Rose" para o tema atual)
    root.style.setProperty("--rose-50", theme.primary);    
    root.style.setProperty("--rose-100", theme.border);    
    root.style.setProperty("--rose-200", theme.border);    
    root.style.setProperty("--rose-300", theme.accent);    
    root.style.setProperty("--rose-400", theme.accent);    
    root.style.setProperty("--rose-500", isDark ? "#ffffff" : theme.foreground); 
    
    // Cores de Neutros
    root.style.setProperty("--zinc-800", theme.foreground);
    root.style.setProperty("--zinc-900", isDark ? "#ffffff" : "#09090b");
  };

  const setGlobalTheme = async (themeKey: string) => {
    await setDoc(doc(db, "settings", "theme"), { currentTheme: themeKey });
  };

  const setLocalTheme = (themeKey: string) => {
    setCurrentTheme(themeKey);
    applyTheme(themeKey);
  };

  return (
    <ThemeContext.Provider value={{ currentTheme, setGlobalTheme, setLocalTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

export const useTheme = () => useContext(ThemeContext);
