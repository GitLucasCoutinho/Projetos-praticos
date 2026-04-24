"use client";

import { useState } from "react";
import { signInWithEmailAndPassword, GoogleAuthProvider, signInWithPopup } from "firebase/auth";
import { auth } from "@/lib/firebase";
import { useRouter } from "next/navigation";
import { LogIn } from "lucide-react";

export default function LoginPage() {
  const [username, setUsername] = useState("ivanete");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    // Map 'ivanete' to the real email
    let email = username;
    if (username === "ivanete") {
      email = "ocrocheencantado@gmail.com";
    }

    try {
      await signInWithEmailAndPassword(auth, email, password);
      router.push("/admin");
    } catch (err: any) {
      console.error("Erro de login:", err.code, err.message);
      setError("Credenciais inválidas. Verifique o usuário e a senha.");
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleLogin = async () => {
    setLoading(true);
    setError("");
    const provider = new GoogleAuthProvider();

    try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;

      // Whitelist only your email
      if (user.email === "ocrocheencantado@gmail.com") {
        router.push("/admin");
      } else {
        await auth.signOut();
        setError("Acesso negado. Apenas o administrador pode entrar.");
      }
    } catch (err: any) {
      console.error("Erro Google Login:", err);
      setError("Erro ao entrar com Google.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-zinc-50 px-4">
      <div className="w-full max-w-md space-y-8 rounded-2xl bg-white p-8 shadow-lg">
        <div className="text-center">
          <h2 className="mt-6 text-3xl font-bold tracking-tight text-zinc-900">
            Painel Administrativo
          </h2>
          <p className="mt-2 text-sm text-zinc-600">
            Entre com suas credenciais para gerenciar o catálogo
          </p>
        </div>

        <div className="mt-8 space-y-6">
          <button
            onClick={handleGoogleLogin}
            disabled={loading}
            className="flex w-full items-center justify-center gap-3 rounded-md border border-zinc-300 bg-white py-2 px-4 text-sm font-medium text-zinc-700 shadow-sm hover:bg-zinc-50 focus:outline-none focus:ring-2 focus:ring-zinc-500 focus:ring-offset-2 disabled:opacity-50"
          >
            <svg className="h-5 w-5" viewBox="0 0 24 24">
              <path
                d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                fill="#4285F4"
              />
              <path
                d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                fill="#34A853"
              />
              <path
                d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z"
                fill="#FBBC05"
              />
              <path
                d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                fill="#EA4335"
              />
            </svg>
            Entrar com Google
          </button>

          <div className="relative">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t border-zinc-300"></div>
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="bg-white px-2 text-zinc-500">ou use usuário e senha</span>
            </div>
          </div>

          <form className="space-y-6" onSubmit={handleLogin}>
          <div className="space-y-4 rounded-md shadow-sm">
            <div>
              <label htmlFor="username" className="sr-only">
                Usuário
              </label>
              <input
                id="username"
                name="username"
                type="text"
                required
                className="relative block w-full rounded-t-md border-0 py-1.5 px-3 text-zinc-900 ring-1 ring-inset ring-zinc-300 placeholder:text-zinc-400 focus:z-10 focus:ring-2 focus:ring-inset focus:ring-zinc-600 sm:text-sm sm:leading-6"
                placeholder="ivanete"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div>
              <label htmlFor="password" className="sr-only">
                Senha
              </label>
              <input
                id="password"
                name="password"
                type="password"
                required
                className="relative block w-full rounded-b-md border-0 py-1.5 px-3 text-zinc-900 ring-1 ring-inset ring-zinc-300 placeholder:text-zinc-400 focus:z-10 focus:ring-2 focus:ring-inset focus:ring-zinc-600 sm:text-sm sm:leading-6"
                placeholder="Senha"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          </div>

          {error && (
            <div className="text-sm text-red-600 text-center">{error}</div>
          )}

          <div>
            <button
              type="submit"
              disabled={loading}
              className="group relative flex w-full justify-center rounded-md bg-zinc-900 py-2 px-3 text-sm font-semibold text-white hover:bg-zinc-800 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-zinc-600 disabled:opacity-50"
            >
              <span className="absolute left-0 inset-y-0 flex items-center pl-3">
                <LogIn className="h-5 w-5 text-zinc-500 group-hover:text-zinc-400" aria-hidden="true" />
              </span>
              {loading ? "Entrando..." : "Entrar"}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  );
}
