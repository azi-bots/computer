import { QueryClient } from '@tanstack/react-query';
import { CACHE_TIME } from './constants';

// 创建React Query客户端配置
export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: CACHE_TIME.SHORT * 1000, // 数据保鲜时间（1分钟）
      gcTime: CACHE_TIME.LONG * 1000, // 缓存保留时间（30分钟）
      retry: 2, // 失败重试次数
      refetchOnWindowFocus: false, // 窗口聚焦时不重新获取
      refetchOnReconnect: true, // 网络重连时重新获取
      refetchOnMount: true, // 组件挂载时重新获取
    },
    mutations: {
      retry: 1, // 失败重试次数
    },
  },
});

// 查询键常量
export const QUERY_KEYS = {
  // 用户相关
  CURRENT_USER: 'currentUser',
  USER_PROFILE: 'userProfile',
  USER_ADDRESSES: 'userAddresses',

  // 商品相关
  PRODUCTS: 'products',
  PRODUCT: 'product',
  PRODUCT_CATEGORIES: 'productCategories',
  PRODUCT_BY_CATEGORY: 'productByCategory',
  PRODUCT_BY_SUPPLIER: 'productBySupplier',

  // 购物车相关
  CART: 'cart',
  CART_ITEMS: 'cartItems',

  // 订单相关
  ORDERS: 'orders',
  ORDER: 'order',
  USER_ORDERS: 'userOrders',
  SUPPLIER_ORDERS: 'supplierOrders',

  // 库存相关
  INVENTORY: 'inventory',
  INVENTORY_BY_PRODUCT: 'inventoryByProduct',

  // 支付相关
  PAYMENTS: 'payments',
  PAYMENT: 'payment',

  // 数据统计
  DASHBOARD_STATS: 'dashboardStats',
  SALES_STATS: 'salesStats',
} as const;

// 预取函数
export const prefetchQueries = {
  // 预取当前用户信息
  currentUser: async () => {
    await queryClient.prefetchQuery({
      queryKey: [QUERY_KEYS.CURRENT_USER],
      queryFn: () => Promise.resolve(null), // 实际应该调用API
    });
  },
} as const;