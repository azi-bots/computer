import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { STORAGE_KEYS } from '@/config/constants';
import type { CartItem, Product } from '@/types';

interface CartState {
  items: CartItem[];
  total: number;
  itemCount: number;
  selectedItems: CartItem[];
}

interface CartActions {
  // 基本操作
  addItem: (product: Product, quantity: number) => void;
  removeItem: (productId: number) => void;
  updateQuantity: (productId: number, quantity: number) => void;
  clearCart: () => void;

  // 选择操作
  toggleSelect: (productId: number) => void;
  selectAll: () => void;
  unselectAll: () => void;

  // 计算操作
  calculateTotal: () => void;
  calculateItemCount: () => void;

  // 批量操作
  removeSelected: () => void;
}

const calculateCartTotals = (items: CartItem[]) => {
  const total = items.reduce((sum, item) => {
    return sum + item.product.price * item.quantity;
  }, 0);

  const itemCount = items.reduce((count, item) => {
    return count + item.quantity;
  }, 0);

  const selectedItems = items.filter((item) => item.selected);

  return { total, itemCount, selectedItems };
};

const initialState: CartState = {
  items: [],
  total: 0,
  itemCount: 0,
  selectedItems: [],
};

export const useCartStore = create<CartState & CartActions>()(
  persist(
    (set, get) => ({
      ...initialState,

      addItem: (product: Product, quantity: number) => {
        const { items } = get();
        const existingItemIndex = items.findIndex((item) => item.productId === product.id);

        let newItems: CartItem[];

        if (existingItemIndex >= 0) {
          // 商品已存在，更新数量
          newItems = [...items];
          newItems[existingItemIndex] = {
            ...newItems[existingItemIndex],
            quantity: newItems[existingItemIndex].quantity + quantity,
            product, // 更新商品信息（价格可能变化）
          };
        } else {
          // 新商品
          const newItem: CartItem = {
            id: Date.now(), // 临时ID，实际应由后端生成
            productId: product.id,
            product,
            quantity,
            userId: 0, // 临时值，实际应从auth store获取
            selected: true,
            addedAt: new Date().toISOString(),
          };
          newItems = [...items, newItem];
        }

        const { total, itemCount, selectedItems } = calculateCartTotals(newItems);
        set({ items: newItems, total, itemCount, selectedItems });
      },

      removeItem: (productId: number) => {
        const { items } = get();
        const newItems = items.filter((item) => item.productId !== productId);

        const { total, itemCount, selectedItems } = calculateCartTotals(newItems);
        set({ items: newItems, total, itemCount, selectedItems });
      },

      updateQuantity: (productId: number, quantity: number) => {
        const { items } = get();
        const itemIndex = items.findIndex((item) => item.productId === productId);

        if (itemIndex >= 0) {
          const newItems = [...items];
          newItems[itemIndex] = {
            ...newItems[itemIndex],
            quantity: Math.max(1, quantity), // 最小数量为1
          };

          const { total, itemCount, selectedItems } = calculateCartTotals(newItems);
          set({ items: newItems, total, itemCount, selectedItems });
        }
      },

      clearCart: () => {
        set(initialState);
      },

      toggleSelect: (productId: number) => {
        const { items } = get();
        const newItems = items.map((item) =>
          item.productId === productId ? { ...item, selected: !item.selected } : item
        );

        const { total, itemCount, selectedItems } = calculateCartTotals(newItems);
        set({ items: newItems, total, itemCount, selectedItems });
      },

      selectAll: () => {
        const { items } = get();
        const newItems = items.map((item) => ({ ...item, selected: true }));

        const { total, itemCount, selectedItems } = calculateCartTotals(newItems);
        set({ items: newItems, total, itemCount, selectedItems });
      },

      unselectAll: () => {
        const { items } = get();
        const newItems = items.map((item) => ({ ...item, selected: false }));

        const { total, itemCount, selectedItems } = calculateCartTotals(newItems);
        set({ items: newItems, total, itemCount, selectedItems });
      },

      calculateTotal: () => {
        const { items } = get();
        const { total, itemCount, selectedItems } = calculateCartTotals(items);
        set({ total, itemCount, selectedItems });
      },

      calculateItemCount: () => {
        const { items } = get();
        const { total, itemCount, selectedItems } = calculateCartTotals(items);
        set({ total, itemCount, selectedItems });
      },

      removeSelected: () => {
        const { items } = get();
        const newItems = items.filter((item) => !item.selected);

        const { total, itemCount, selectedItems } = calculateCartTotals(newItems);
        set({ items: newItems, total, itemCount, selectedItems });
      },
    }),
    {
      name: 'cart-storage',
      partialize: (state) => ({
        items: state.items,
        total: state.total,
        itemCount: state.itemCount,
        selectedItems: state.selectedItems,
      }),
    }
  )
);

// 导出hooks
export const useCartItems = () => useCartStore((state) => state.items);
export const useCartTotal = () => useCartStore((state) => state.total);
export const useCartItemCount = () => useCartStore((state) => state.itemCount);
export const useSelectedItems = () => useCartStore((state) => state.selectedItems);
export const useCartActions = () => {
  const {
    addItem,
    removeItem,
    updateQuantity,
    clearCart,
    toggleSelect,
    selectAll,
    unselectAll,
    removeSelected,
  } = useCartStore();
  return {
    addItem,
    removeItem,
    updateQuantity,
    clearCart,
    toggleSelect,
    selectAll,
    unselectAll,
    removeSelected,
  };
};