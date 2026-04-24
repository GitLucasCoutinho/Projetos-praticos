"use client";

import { useEffect, useState } from "react";
import { db } from "@/lib/firebase";
import { collection, query, orderBy, onSnapshot } from "firebase/firestore";
import ProductCard from "@/components/ProductCard";
import { Loader2, LogIn, Palette, Sun, Moon, Check } from "lucide-react";
import Link from "next/link";
import { useTheme, themes } from "@/context/ThemeContext";

interface Product {
  id: string;
  name: string;
  description: string;
  weight: string;
  size: {
    width: string;
    length: string;
  };
  price: string;
  imageUrl: string;
}

export default function Home() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [showThemeMenu, setShowThemeMenu] = useState(false);
  const { currentTheme, setLocalTheme } = useTheme();

  useEffect(() => {
    const q = query(collection(db, "products"), orderBy("createdAt", "desc"));
    
    const unsubscribe = onSnapshot(q, (querySnapshot) => {
      const prods: Product[] = [];
      querySnapshot.forEach((doc) => {
        prods.push({ id: doc.id, ...doc.data() } as Product);
      });
      setProducts(prods);
      setLoading(false);
    });

    return () => unsubscribe();
  }, []);

  return (
    <div className="min-h-screen bg-background transition-colors duration-500">
      {/* Hero Section */}
      <header className="py-16 px-6 bg-card/80 backdrop-blur-sm border-b border-rose-100 relative z-[100] transition-colors">
        <div className="absolute top-6 right-6 flex items-center gap-4">
          {/* Theme Picker Dropdown */}
          <div className="relative">
            <button
              onClick={() => setShowThemeMenu(!showThemeMenu)}
              className={`p-2.5 rounded-full transition-all border ${
                showThemeMenu 
                  ? "bg-rose-50 border-rose-200 text-rose-500 shadow-inner" 
                  : "hover:bg-rose-50 border-transparent text-zinc-600 hover:border-rose-100"
              }`}
              title="Mudar Tema"
            >
              <Palette size={18} />
            </button>

            {showThemeMenu && (
              <>
                <div className="fixed inset-0 z-40" onClick={() => setShowThemeMenu(false)} />
                <div className="absolute top-full right-0 mt-3 w-56 bg-card rounded-[1.5rem] shadow-2xl border border-rose-100/20 py-3 z-50 animate-in fade-in zoom-in duration-200 origin-top-right transition-colors">
                  <div className="px-5 py-2 mb-2 border-b border-rose-100/10 flex items-center justify-between">
                    <span className="text-[10px] font-bold uppercase tracking-widest text-zinc-400">Cores</span>
                    <Palette size={12} className="text-rose-300" />
                  </div>
                  <div className="px-3 grid grid-cols-3 gap-2">
                    {Object.entries(themes).map(([key, theme]) => (
                      <button
                        key={key}
                        onClick={() => {
                          setLocalTheme(key);
                          setShowThemeMenu(false);
                        }}
                        className={`relative flex flex-col items-center justify-center p-2 rounded-xl transition-all group ${
                          currentTheme === key ? "bg-rose-50/20" : "hover:bg-rose-50/10"
                        }`}
                        title={theme.name}
                      >
                        <div 
                          className={`w-8 h-8 rounded-full border border-black/5 shadow-sm transition-transform group-hover:scale-110 ${
                            currentTheme === key ? "ring-2 ring-rose-400 ring-offset-2 ring-offset-background" : ""
                          }`}
                          style={{ backgroundColor: theme.primary }}
                        />
                        <div className="absolute -top-1 -right-1 bg-card rounded-full p-0.5 shadow-sm border border-rose-100/20 transition-colors">
                          {key.includes("dark") || key.includes("midnight") || key.includes("forest") 
                            ? <Moon size={10} className="text-rose-400" /> 
                            : <Sun size={10} className="text-rose-400" />
                          }
                        </div>
                        {currentTheme === key && (
                          <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
                            <Check size={14} className="text-rose-500" />
                          </div>
                        )}
                      </button>
                    ))}
                  </div>
                </div>
              </>
            )}
          </div>

          <div className="h-6 w-px bg-zinc-200" />

          <Link 
            href="/admin" 
            className="flex items-center gap-2 text-xs font-medium text-rose-300 hover:text-rose-400 transition-colors uppercase tracking-widest"
          >
            <LogIn size={14} /> Área Administrativa
          </Link>
        </div>
        <div className="max-w-4xl mx-auto text-center">
          <div className="inline-block px-4 py-1 rounded-full bg-rose-50 text-rose-400 text-xs font-semibold tracking-widest uppercase mb-4 transition-colors">
            Feito à Mão com Amor
          </div>
          <h1 className="text-5xl md:text-6xl font-serif font-bold text-zinc-900 mb-6 tracking-tight transition-colors">
            Crochê <span className="text-rose-300 transition-colors">Encantado</span>
          </h1>
          <p className="text-lg text-zinc-500 font-light max-w-2xl mx-auto italic leading-relaxed transition-colors">
            Transformando fios em poesia para decorar o seu lar com o aconchego que você merece.
          </p>
        </div>
      </header>

      {/* Product List */}
      <main className="max-w-6xl mx-auto px-6 py-20">
        {loading ? (
          <div className="flex flex-col items-center justify-center py-20 gap-4">
            <Loader2 className="animate-spin text-zinc-300" size={48} />
            <p className="text-zinc-400 animate-pulse">Carregando catálogo encantado...</p>
          </div>
        ) : products.length > 0 ? (
          <div className="grid grid-cols-1 gap-12">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        ) : (
          <div className="text-center py-40">
            <p className="text-2xl text-zinc-400 font-light italic">
              Nenhum produto cadastrado no momento.
            </p>
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="py-12 border-t border-rose-100 bg-card text-center transition-colors">
        <p className="text-zinc-400 text-sm">
          &copy; {new Date().getFullYear()} Crochê Encantado. Todos os direitos reservados.
        </p>
      </footer>
    </div>
  );
}
