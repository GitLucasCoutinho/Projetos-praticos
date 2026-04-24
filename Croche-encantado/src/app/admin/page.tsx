"use client";

import { useState, useEffect } from "react";
import { useAuth } from "@/hooks/useAuth";
import { db } from "@/lib/firebase";
import { collection, addDoc, serverTimestamp, onSnapshot, query, orderBy, doc, updateDoc, deleteDoc } from "firebase/firestore";
import { useRouter } from "next/navigation";
import { Upload, Plus, LogOut, Edit2, Trash2, X, Palette, Check, Sun, Moon } from "lucide-react";
import { auth } from "@/lib/firebase";
import { signOut } from "firebase/auth";
import { useTheme, themes } from "@/context/ThemeContext";
import Link from "next/link";

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

export default function AdminPage() {
  const { user, loading: authLoading } = useAuth(true);
  const router = useRouter();
  
  const [products, setProducts] = useState<Product[]>([]);
  const [editingId, setEditingId] = useState<string | null>(null);
  
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [weight, setWeight] = useState("");
  const [width, setWidth] = useState("");
  const [length, setLength] = useState("");
  const [price, setPrice] = useState("");
  const [image, setImage] = useState<File | null>(null);
  const [existingImageUrl, setExistingImageUrl] = useState("");
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState("");

  const { currentTheme, setGlobalTheme } = useTheme();
  const [showThemeMenu, setShowThemeMenu] = useState(false);

  useEffect(() => {
    if (!user) return;
    const q = query(collection(db, "products"), orderBy("createdAt", "desc"));
    const unsubscribe = onSnapshot(q, (snapshot) => {
      const prods = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() } as Product));
      setProducts(prods);
    });
    return () => unsubscribe();
  }, [user]);

  const handleLogout = async () => {
    await signOut(auth);
    router.push("/admin/login");
  };

  const compressImage = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = (event) => {
        const img = new Image();
        img.src = event.target?.result as string;
        img.onload = () => {
          const canvas = document.createElement("canvas");
          let width = img.width;
          let height = img.height;
          const MAX_WIDTH = 1024;
          const MAX_HEIGHT = 1024;
          if (width > height) {
            if (width > MAX_WIDTH) {
              height *= MAX_WIDTH / width;
              width = MAX_WIDTH;
            }
          } else {
            if (height > MAX_HEIGHT) {
              width *= MAX_HEIGHT / height;
              height = MAX_HEIGHT;
            }
          }
          canvas.width = width;
          canvas.height = height;
          const ctx = canvas.getContext("2d");
          ctx?.drawImage(img, 0, 0, width, height);
          const dataUrl = canvas.toDataURL("image/jpeg", 0.7);
          resolve(dataUrl);
        };
        img.onerror = (err) => reject(err);
      };
      reader.onerror = (err) => reject(err);
    });
  };

  const handleEdit = (product: Product) => {
    setEditingId(product.id);
    setName(product.name);
    setDescription(product.description);
    setWeight(product.weight);
    setWidth(product.size.width);
    setLength(product.size.length);
    setPrice(product.price);
    setExistingImageUrl(product.imageUrl);
    setImage(null);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const cancelEdit = () => {
    setEditingId(null);
    setName("");
    setDescription("");
    setWeight("");
    setWidth("");
    setLength("");
    setPrice("");
    setImage(null);
    setExistingImageUrl("");
  };

  const handleDelete = async (id: string) => {
    if (confirm("Tem certeza que deseja excluir este tapete?")) {
      try {
        await deleteDoc(doc(db, "products", id));
      } catch (err) {
        console.error("Erro ao excluir:", err);
        alert("Erro ao excluir produto.");
      }
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!image && !existingImageUrl) {
      alert("Por favor, selecione uma imagem.");
      return;
    }

    setUploading(true);
    setError("");
    try {
      let finalImageUrl = existingImageUrl;
      if (image) {
        finalImageUrl = await compressImage(image);
      }

      const productData = {
        name,
        description,
        weight,
        size: { width, length },
        price,
        imageUrl: finalImageUrl,
        updatedAt: serverTimestamp(),
      };

      if (editingId) {
        await updateDoc(doc(db, "products", editingId), productData);
        setEditingId(null);
      } else {
        await addDoc(collection(db, "products"), {
          ...productData,
          createdAt: serverTimestamp(),
        });
      }

      // Reset Form
      setName("");
      setDescription("");
      setWeight("");
      setWidth("");
      setLength("");
      setPrice("");
      setImage(null);
      setExistingImageUrl("");
      alert(editingId ? "Produto atualizado!" : "Produto adicionado!");
    } catch (err: any) {
      console.error("Erro ao salvar produto:", err);
      setError(`Erro ao salvar: ${err.message || "Verifique as permissões."}`);
    } finally {
      setUploading(false);
    }
  };

  if (authLoading) return <div className="flex min-h-screen items-center justify-center">Carregando...</div>;
  if (!user) return null;

  return (
    <div className="min-h-screen bg-background pb-12 transition-colors duration-500">
      <header className="bg-white/70 backdrop-blur-md shadow-sm border-b border-rose-100 sticky top-0 z-[100]">
        <div className="mx-auto max-w-7xl px-4 py-4 sm:px-6 lg:px-8 flex justify-between items-center">
          <Link href="/" className="group">
            <h1 className="text-2xl font-serif font-bold tracking-tight text-zinc-800 group-hover:text-rose-500 transition-colors">
              Crochê <span className="text-rose-400 group-hover:text-rose-600 transition-colors">Encantado</span>
            </h1>
          </Link>
          
          <div className="flex items-center gap-4 relative">
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
                <Palette size={20} />
              </button>

              {showThemeMenu && (
                <>
                  <div className="fixed inset-0 z-40" onClick={() => setShowThemeMenu(false)} />
                  <div className="absolute top-full right-0 mt-3 w-56 bg-card rounded-[1.5rem] shadow-2xl border border-rose-100/20 py-3 z-50 animate-in fade-in zoom-in duration-200 origin-top-right transition-colors">
                    <div className="px-5 py-2 mb-2 border-b border-rose-100/10 flex items-center justify-between">
                      <span className="text-[10px] font-bold uppercase tracking-widest text-zinc-400">Personalizar</span>
                      <Palette size={12} className="text-rose-300" />
                    </div>
                    <div className="px-3 grid grid-cols-3 gap-2">
                      {Object.entries(themes).map(([key, theme]) => (
                        <button
                          key={key}
                          onClick={() => {
                            setGlobalTheme(key);
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

            <button
              onClick={handleLogout}
              className="flex items-center gap-2 text-sm font-medium text-rose-400 hover:text-rose-500 bg-rose-50 px-4 py-2 rounded-full transition-colors"
            >
              <LogOut size={16} /> Sair
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-3xl mt-10 px-4">
        {error && (
          <div className="mb-6 rounded-2xl bg-red-50 p-4 text-sm text-red-600 border border-red-100 shadow-sm">
            {error}
          </div>
        )}
        <div className="bg-white/80 backdrop-blur-sm rounded-[2rem] shadow-xl shadow-rose-100/20 border border-rose-50 p-8">
          <div className="flex items-center justify-between mb-6">
            <div className="flex items-center gap-2 text-zinc-800">
              {editingId ? <Edit2 className="text-rose-300" /> : <Plus className="text-rose-300" />}
              <h2 className="text-xl font-serif font-semibold">
                {editingId ? "Editar Tapete" : "Adicionar Novo Tapete"}
              </h2>
            </div>
            {editingId && (
              <button
                onClick={cancelEdit}
                className="text-sm font-medium text-zinc-500 hover:text-zinc-700 flex items-center gap-1"
              >
                <X size={14} /> Cancelar Edição
              </button>
            )}
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
              <div className="sm:col-span-2">
                <label className="block text-sm font-medium text-zinc-700">Nome do Produto</label>
                <input
                  type="text"
                  required
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="Ex: Tapete Peludinho"
                  className="mt-1 block w-full rounded-lg border-zinc-200 bg-zinc-50 px-4 py-2 text-zinc-900 focus:border-rose-300 focus:ring-rose-300 sm:text-sm border transition-all"
                />
              </div>

              <div className="sm:col-span-2">
                <label className="block text-sm font-medium text-zinc-700">Descrição</label>
                <textarea
                  rows={3}
                  required
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder="Ex: 01 - Peludinho mel, cru e bege - três bicos"
                  className="mt-1 block w-full rounded-lg border-zinc-200 bg-zinc-50 px-4 py-2 text-zinc-900 focus:border-rose-300 focus:ring-rose-300 sm:text-sm border transition-all"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-zinc-700">Peso (gramas)</label>
                <input
                  type="text"
                  required
                  value={weight}
                  onChange={(e) => setWeight(e.target.value)}
                  placeholder="Ex: 498"
                  className="mt-1 block w-full rounded-lg border-zinc-200 bg-zinc-50 px-4 py-2 text-zinc-900 focus:border-rose-300 focus:ring-rose-300 sm:text-sm border transition-all"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-zinc-700">Preço (R$)</label>
                <input
                  type="text"
                  required
                  value={price}
                  onChange={(e) => setPrice(e.target.value)}
                  placeholder="Ex: 70,00"
                  className="mt-1 block w-full rounded-lg border-zinc-200 bg-zinc-50 px-4 py-2 text-zinc-900 focus:border-rose-300 focus:ring-rose-300 sm:text-sm border transition-all"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-zinc-700">Largura (cm)</label>
                <input
                  type="text"
                  required
                  value={width}
                  onChange={(e) => setWidth(e.target.value)}
                  placeholder="Ex: 50"
                  className="mt-1 block w-full rounded-lg border-zinc-200 bg-zinc-50 px-4 py-2 text-zinc-900 focus:border-rose-300 focus:ring-rose-300 sm:text-sm border transition-all"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-zinc-700">Comprimento (cm)</label>
                <input
                  type="text"
                  required
                  value={length}
                  onChange={(e) => setLength(e.target.value)}
                  placeholder="Ex: 75"
                  className="mt-1 block w-full rounded-lg border-zinc-200 bg-zinc-50 px-4 py-2 text-zinc-900 focus:border-rose-300 focus:ring-rose-300 sm:text-sm border transition-all"
                />
              </div>

              <div className="sm:col-span-2">
                <label className="block text-sm font-medium text-zinc-700">Foto do Tapete</label>
                <div className="mt-1 flex justify-center rounded-2xl border border-dashed border-rose-200 px-6 py-10 bg-rose-50/30 transition-colors hover:bg-rose-50/50">
                  <div className="text-center">
                    <Upload className="mx-auto h-12 w-12 text-rose-200" aria-hidden="true" />
                    <div className="mt-4 flex text-sm leading-6 text-zinc-600">
                      <label className="relative cursor-pointer rounded-md font-semibold text-rose-400 focus-within:outline-none hover:text-rose-500">
                        <span>{editingId ? "Trocar foto" : "Carregar arquivo"}</span>
                        <input
                          type="file"
                          className="sr-only"
                          accept="image/*"
                          onChange={(e) => setImage(e.target.files?.[0] || null)}
                        />
                      </label>
                      <p className="pl-1 text-zinc-400">ou arraste e solte</p>
                    </div>
                    {image ? (
                      <p className="mt-2 text-sm font-medium text-rose-400">
                        Nova foto: {image.name}
                      </p>
                    ) : existingImageUrl && editingId ? (
                      <p className="mt-2 text-sm font-medium text-zinc-400 italic">
                        Mantendo foto atual
                      </p>
                    ) : null}
                  </div>
                </div>
              </div>
            </div>

            <div className="pt-4">
              <button
                type="submit"
                disabled={uploading}
                className="flex w-full justify-center rounded-full bg-zinc-900 px-4 py-4 text-sm font-semibold text-white shadow-lg shadow-zinc-200 hover:bg-zinc-800 transition-all focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-zinc-600 disabled:opacity-50"
              >
                {uploading ? "Salvando..." : editingId ? "Atualizar Tapete" : "Salvar Tapete"}
              </button>
            </div>
          </form>
        </div>

        {/* Product List Section */}
        <div className="mt-16">
          <h2 className="text-2xl font-serif font-bold text-zinc-800 mb-8 px-2 flex items-center gap-2">
            Tapetes Cadastrados
            <span className="text-sm font-sans font-medium text-zinc-400 bg-white px-2 py-0.5 rounded-full border border-zinc-100">
              {products.length}
            </span>
          </h2>

          <div className="space-y-4">
            {products.map((product) => (
              <div
                key={product.id}
                className="bg-white/60 backdrop-blur-sm rounded-3xl p-4 border border-rose-50 flex items-center gap-6 shadow-sm hover:shadow-md transition-all group"
              >
                <div className="w-20 h-20 rounded-2xl overflow-hidden relative flex-shrink-0">
                  <img
                    src={product.imageUrl}
                    alt={product.name}
                    className="object-cover w-full h-full"
                  />
                </div>
                
                <div className="flex-1 min-w-0">
                  <h3 className="text-lg font-serif font-bold text-zinc-800 truncate">
                    {product.name}
                  </h3>
                  <p className="text-sm text-zinc-400 truncate">
                    {product.description}
                  </p>
                </div>

                <div className="flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity pr-4">
                  <button
                    onClick={() => handleEdit(product)}
                    className="p-3 rounded-full bg-rose-50 text-rose-400 hover:bg-rose-100 transition-colors"
                    title="Editar"
                  >
                    <Edit2 size={18} />
                  </button>
                  <button
                    onClick={() => handleDelete(product.id)}
                    className="p-3 rounded-full bg-red-50 text-red-400 hover:bg-red-100 transition-colors"
                    title="Excluir"
                  >
                    <Trash2 size={18} />
                  </button>
                </div>
              </div>
            ))}

            {products.length === 0 && (
              <div className="text-center py-12 bg-white/40 rounded-[2rem] border border-dashed border-zinc-200">
                <p className="text-zinc-400 italic">Nenhum tapete encontrado.</p>
              </div>
            )}
          </div>
        </div>
      </main>
    </div>
  );
}
