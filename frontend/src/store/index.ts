// 状态存储统一导出
export { useAuthStore } from './auth.store';
export { useCartStore } from './cart.store';
export { useUIStore } from './ui.store';

// 组合store hooks
import { useAuthStore } from './auth.store';
import { useCartStore } from './cart.store';
import { useUIStore } from './ui.store';

export const useStore = () => {
  const auth = useAuthStore();
  const cart = useCartStore();
  const ui = useUIStore();

  return {
    auth,
    cart,
    ui,
  };
};