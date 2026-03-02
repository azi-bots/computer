// 基础类型定义
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
  timestamp: string;
}

export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string;
  order?: 'asc' | 'desc';
}

export interface PaginationResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
  pages: number;
}

// 用户相关类型
export interface User {
  id: number;
  username: string;
  email: string;
  phone?: string;
  avatar?: string;
  role: 'USER' | 'ADMIN' | 'SUPPLIER';
  status: 'ACTIVE' | 'INACTIVE' | 'BANNED';
  createdAt: string;
  updatedAt: string;
}

export interface AuthResponse {
  token: string;
  user: User;
  expiresIn: number;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest extends LoginRequest {
  email: string;
  phone?: string;
}

// 商品相关类型
export interface Product {
  id: number;
  name: string;
  description?: string;
  price: number;
  stock: number;
  categoryId: number;
  supplierId: number;
  images?: string[];
  specs?: Record<string, any>;
  status: 'AVAILABLE' | 'OUT_OF_STOCK' | 'DISCONTINUED';
  createdAt: string;
  updatedAt: string;
}

export interface Category {
  id: number;
  name: string;
  description?: string;
  parentId?: number;
  level: number;
  sortOrder: number;
  status: 'ACTIVE' | 'INACTIVE';
}

// 购物车相关类型
export interface CartItem {
  id: number;
  productId: number;
  product: Product;
  quantity: number;
  userId: number;
  selected: boolean;
  addedAt: string;
}

// 订单相关类型
export interface Order {
  id: number;
  orderNo: string;
  userId: number;
  totalAmount: number;
  status: 'PENDING' | 'PAID' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'REFUNDED';
  paymentMethod?: string;
  shippingAddress?: string;
  remark?: string;
  createdAt: string;
  updatedAt: string;
}

export interface OrderItem {
  id: number;
  orderId: number;
  productId: number;
  product: Product;
  quantity: number;
  price: number;
  subtotal: number;
}

// 地址相关类型
export interface UserAddress {
  id: number;
  userId: number;
  recipient: string;
  phone: string;
  province: string;
  city: string;
  district: string;
  detail: string;
  isDefault: boolean;
  createdAt: string;
  updatedAt: string;
}

// 库存相关类型
export interface Inventory {
  id: number;
  productId: number;
  quantity: number;
  warehouse?: string;
  location?: string;
  status: 'IN_STOCK' | 'LOW_STOCK' | 'OUT_OF_STOCK';
  updatedAt: string;
}

// 支付相关类型
export interface Payment {
  id: number;
  orderId: number;
  amount: number;
  method: 'ALIPAY' | 'WECHAT' | 'BANK_TRANSFER' | 'CASH';
  status: 'PENDING' | 'SUCCESS' | 'FAILED';
  transactionId?: string;
  paidAt?: string;
  createdAt: string;
}

// UI相关类型
export interface Notification {
  id: string;
  type: 'success' | 'error' | 'warning' | 'info';
  title: string;
  message: string;
  duration?: number;
}

// 路由权限类型
export interface RoutePermission {
  path: string;
  roles: ('USER' | 'ADMIN' | 'SUPPLIER')[];
  title: string;
}