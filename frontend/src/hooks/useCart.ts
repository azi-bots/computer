import { useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { cartApi } from '@/api';
import { useCartStore, useAuthStore } from '@/store';
import { QUERY_KEYS } from '@/config/queryClient';

/**
 * 购物车相关自定义hook
 */
const useCart = () => {
  const queryClient = useQueryClient();
  const { items, total, itemCount, selectedItems, ...cartActions } = useCartStore();
  const { isAuthenticated } = useAuthStore();

  // 获取服务器购物车数据
  const {
    data: serverCartItems,
    isLoading,
    error,
    refetch,
  } = useQuery({
    queryKey: [QUERY_KEYS.CART_ITEMS],
    queryFn: () => cartApi.getCartItems(),
    enabled: isAuthenticated, // 只有登录后才从服务器获取
  });

  // 同步本地购物车到服务器
  const syncCartMutation = useMutation({
    mutationFn: cartApi.syncCart,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.CART_ITEMS] });
    },
  });

  // 添加商品到购物车（服务器）
  const addToCartMutation = useMutation({
    mutationFn: cartApi.addToCart,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.CART_ITEMS] });
    },
  });

  // 更新购物车商品数量（服务器）
  const updateCartItemMutation = useMutation({
    mutationFn: ({ id, quantity }: { id: number; quantity: number }) =>
      cartApi.updateCartItem(id, quantity),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.CART_ITEMS] });
    },
  });

  // 从购物车删除商品（服务器）
  const removeFromCartMutation = useMutation({
    mutationFn: cartApi.removeFromCart,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.CART_ITEMS] });
    },
  });

  // 清空购物车（服务器）
  const clearCartMutation = useMutation({
    mutationFn: cartApi.clearCart,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEYS.CART_ITEMS] });
      cartActions.clearCart();
    },
  });

  // 登录后同步本地购物车到服务器
  useEffect(() => {
    if (isAuthenticated && items.length > 0) {
      const itemsToSync = items.map((item) => ({
        productId: item.productId,
        quantity: item.quantity,
      }));
      syncCartMutation.mutate(itemsToSync);
    }
  }, [isAuthenticated]);

  // 登录后使用服务器购物车数据
  useEffect(() => {
    if (isAuthenticated && serverCartItems && serverCartItems.records) {
      // 这里可以将服务器数据合并到本地store
      // 暂时简单处理：使用服务器数据
    }
  }, [isAuthenticated, serverCartItems]);

  // 添加商品到购物车（统一处理）
  const addItem = async (productId: number, quantity: number) => {
    if (isAuthenticated) {
      await addToCartMutation.mutateAsync({ productId, quantity });
    } else {
      // 本地处理
      // 需要先获取商品信息，这里简化处理
      // cartActions.addItem(product, quantity);
    }
  };

  // 更新购物车商品数量（统一处理）
  const updateItemQuantity = async (itemId: number, quantity: number) => {
    if (isAuthenticated) {
      await updateCartItemMutation.mutateAsync({ id: itemId, quantity });
    } else {
      // 本地处理
      // cartActions.updateQuantity(productId, quantity);
    }
  };

  // 从购物车删除商品（统一处理）
  const removeItem = async (itemId: number) => {
    if (isAuthenticated) {
      await removeFromCartMutation.mutateAsync(itemId);
    } else {
      // 本地处理
      // cartActions.removeItem(productId);
    }
  };

  // 清空购物车（统一处理）
  const clear = async () => {
    if (isAuthenticated) {
      await clearCartMutation.mutateAsync();
    } else {
      cartActions.clearCart();
    }
  };

  // 计算选中商品总价
  const selectedTotal = selectedItems.reduce((sum, item) => {
    return sum + item.product.price * item.quantity;
  }, 0);

  // 计算选中商品数量
  const selectedCount = selectedItems.reduce((count, item) => {
    return count + item.quantity;
  }, 0);

  // 检查商品是否在购物车中
  const isInCart = (productId: number): boolean => {
    return items.some((item) => item.productId === productId);
  };

  // 获取购物车中商品数量
  const getProductQuantity = (productId: number): number => {
    const item = items.find((item) => item.productId === productId);
    return item ? item.quantity : 0;
  };

  return {
    // 状态
    items: isAuthenticated ? serverCartItems?.records || items : items,
    total,
    itemCount,
    selectedItems,
    selectedTotal,
    selectedCount,
    isLoading,
    error,

    // 操作
    addItem,
    updateItemQuantity,
    removeItem,
    clear,
    refetch,

    // 购物车store操作
    ...cartActions,

    // 工具方法
    isInCart,
    getProductQuantity,
    hasItems: items.length > 0,
    isEmpty: items.length === 0,

    // 突变状态
    isSyncing: syncCartMutation.isPending,
    isAdding: addToCartMutation.isPending,
    isUpdating: updateCartItemMutation.isPending,
    isRemoving: removeFromCartMutation.isPending,
    isClearing: clearCartMutation.isPending,
  };
};

export default useCart;