"use client";
import { createContext, useContext, useEffect, useState, ReactNode } from "react";
import { apiFetch } from "@/lib/api";
import { useRouter } from "next/navigation";

interface User {
  nickname: string;
}

interface AuthContextType {
  token: string | null;
  user: User | null;
  signup: (email: string, password: string, nickname: string) => Promise<void>;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(null);
  const [user, setUser] = useState<User | null>(null);
  const router = useRouter();

  useEffect(() => {
    const savedToken = localStorage.getItem("token");
    const savedNickname = localStorage.getItem("nickname");
    if (savedToken) setToken(savedToken);
    if (savedNickname) setUser({ nickname: savedNickname });
  }, []);

  const saveAuth = (data: { token: string; nickname: string }) => {
    localStorage.setItem("token", data.token);
    localStorage.setItem("nickname", data.nickname);
    setToken(data.token);
    setUser({ nickname: data.nickname });
  };

  const signup = async (email: string, password: string, nickname: string) => {
    const data = await apiFetch("/api/auth/signup", {
      method: "POST",
      body: JSON.stringify({ email, password, nickname }),
    });
    saveAuth(data);
  };

  const login = async (email: string, password: string) => {
    const data = await apiFetch("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password }),
    });
    saveAuth(data);
  };

  const logout = async () => {
  try {
    await apiFetch("/api/auth/logout", { method: "POST" });
  } catch {
  }
  localStorage.removeItem("token");
  localStorage.removeItem("nickname");
  setToken(null);
  setUser(null);
  router.push("/");
};

  return (
    <AuthContext.Provider value={{ token, user, signup, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}