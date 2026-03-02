// 应用常量配置

// API配置
export const API_CONFIG = {
  BASE_URL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  TIMEOUT: 10000,
  RETRY_COUNT: 3,
} as const;

// 存储键名
export const STORAGE_KEYS = {
  TOKEN: 'auth_token',
  USER: 'auth_user',
  CART: 'cart_items',
  THEME: 'app_theme',
  LANGUAGE: 'app_language',
} as const;

// 路由路径
export const ROUTE_PATHS = {
  // 公共路由
  HOME: '/',
  LOGIN: '/login',
  REGISTER: '/register',
  PRODUCTS: '/products',
  PRODUCT_DETAIL: '/products/:id',
  CATEGORIES: '/categories',

  // 用户路由
  CART: '/cart',
  CHECKOUT: '/checkout',
  ORDERS: '/orders',
  ORDER_DETAIL: '/orders/:id',
  PROFILE: '/profile',
  ADDRESSES: '/addresses',

  // 管理路由
  ADMIN: '/admin',
  ADMIN_DASHBOARD: '/admin/dashboard',
  ADMIN_PRODUCTS: '/admin/products',
  ADMIN_ORDERS: '/admin/orders',
  ADMIN_USERS: '/admin/users',
  ADMIN_CATEGORIES: '/admin/categories',

  // 供应商路由
  SUPPLIER: '/supplier',
  SUPPLIER_DASHBOARD: '/supplier/dashboard',
  SUPPLIER_INVENTORY: '/supplier/inventory',
  SUPPLIER_ORDERS: '/supplier/orders',
  SUPPLIER_PRODUCTS: '/supplier/products',
} as const;

// 用户角色
export const USER_ROLES = {
  USER: 'USER',
  ADMIN: 'ADMIN',
  SUPPLIER: 'SUPPLIER',
} as const;

// 订单状态
export const ORDER_STATUS = {
  PENDING: 'PENDING',
  PAID: 'PAID',
  SHIPPED: 'SHIPPED',
  DELIVERED: 'DELIVERED',
  CANCELLED: 'CANCELLED',
  REFUNDED: 'REFUNDED',
} as const;

// 商品状态
export const PRODUCT_STATUS = {
  AVAILABLE: 'AVAILABLE',
  OUT_OF_STOCK: 'OUT_OF_STOCK',
  DISCONTINUED: 'DISCONTINUED',
} as const;

// 支付方式
export const PAYMENT_METHODS = {
  ALIPAY: 'ALIPAY',
  WECHAT: 'WECHAT',
  BANK_TRANSFER: 'BANK_TRANSFER',
  CASH: 'CASH',
} as const;

// 分页配置
export const PAGINATION_CONFIG = {
  DEFAULT_PAGE_SIZE: 10,
  PAGE_SIZE_OPTIONS: [10, 20, 50, 100],
  DEFAULT_CURRENT_PAGE: 1,
} as const;

// 主题配置
export const THEME_CONFIG = {
  LIGHT: 'light',
  DARK: 'dark',
  DEFAULT: 'light',
} as const;

// 缓存时间（秒）
export const CACHE_TIME = {
  SHORT: 60, // 1分钟
  MEDIUM: 300, // 5分钟
  LONG: 1800, // 30分钟
  VERY_LONG: 3600, // 1小时
} as const;

// 表单验证规则
export const VALIDATION_RULES = {
  USERNAME: {
    MIN: 3,
    MAX: 20,
    PATTERN: /^[a-zA-Z0-9_]+$/,
  },
  PASSWORD: {
    MIN: 6,
    MAX: 30,
    PATTERN: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{6,}$/,
  },
  EMAIL: {
    PATTERN: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  },
  PHONE: {
    PATTERN: /^1[3-9]\d{9}$/,
  },
} as const;