import Image from "next/image";
import { Ruler, Weight, Tag } from "lucide-react";

interface ProductCardProps {
  product: {
    name: string;
    description: string;
    weight: string;
    size: {
      width: string;
      length: string;
    };
    price: string;
    imageUrl: string;
  };
  onImageClick?: (imageUrl: string) => void;
}

export default function ProductCard({ product, onImageClick }: ProductCardProps) {
  return (
    <div className="group bg-card backdrop-blur-md rounded-[2.5rem] overflow-hidden shadow-sm border border-rose-100 hover:shadow-xl hover:shadow-rose-100/20 transition-all duration-500">
      <div className="flex flex-col md:flex-row min-h-[350px]">
        {/* Information Section */}
        <div className="flex-1 p-10 flex flex-col justify-center order-1">
          <div className="w-12 h-1 bg-rose-300 rounded-full mb-6"></div>
          <h2 className="text-3xl font-serif font-bold text-zinc-900 mb-4 transition-colors">
            {product.name}
          </h2>
          
          <p className="text-zinc-500 leading-relaxed mb-8 italic text-lg transition-colors">
            {product.description}
          </p>

          <div className="grid grid-cols-2 gap-4 mb-8">
            <div className="flex flex-col p-4 rounded-2xl bg-rose-50 border border-rose-100 transition-colors">
              <div className="flex items-center gap-2 text-rose-400 mb-1">
                <Weight size={16} />
                <span className="text-xs uppercase tracking-wider font-bold">Peso</span>
              </div>
              <span className="text-zinc-800 font-medium">{product.weight}g</span>
            </div>
            <div className="flex flex-col p-4 rounded-2xl bg-rose-50 border border-rose-100 transition-colors">
              <div className="flex items-center gap-2 text-rose-400 mb-1">
                <Ruler size={16} />
                <span className="text-xs uppercase tracking-wider font-bold">Tamanho</span>
              </div>
              <span className="text-zinc-800 font-medium">
                {product.size.width}x{product.size.length}cm
              </span>
            </div>
          </div>

          <div className="flex items-center justify-between mt-auto">
            <div className="flex items-center gap-2 bg-zinc-900 text-white px-6 py-3 rounded-full shadow-lg border border-white/10">
              <Tag size={18} className="text-rose-300" />
              <span className="text-xl font-bold italic">R$ {product.price}</span>
            </div>
          </div>
        </div>

        {/* Image Section */}
        <div 
          className="relative flex-1 min-h-[350px] md:min-h-auto order-2 overflow-hidden cursor-zoom-in group/image"
          onClick={() => onImageClick?.(product.imageUrl)}
        >
          <Image
            src={product.imageUrl}
            alt={product.name}
            fill
            className="object-cover group-hover/image:scale-110 transition-transform duration-700"
            sizes="(max-width: 768px) 100vw, 50vw"
          />
          <div className="absolute inset-0 bg-black/0 group-hover/image:bg-black/10 transition-colors duration-500 flex items-center justify-center">
            <div className="opacity-0 group-hover/image:opacity-100 transform translate-y-4 group-hover/image:translate-y-0 transition-all duration-500">
              <span className="bg-white/90 backdrop-blur-sm text-zinc-900 px-4 py-2 rounded-full text-sm font-medium shadow-lg">
                Ver em Tamanho Real
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
