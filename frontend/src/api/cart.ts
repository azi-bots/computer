import { http } from '@/config/axios';
import type { ApiResponse, CartItem, PaginationParams, PaginationResult } from '@/types';

// 购物车相关API
export const cartApi = {
  // 获取购物车列表
  getCartItems: (params?: PaginationParams): Promise<PaginationResult<CartItem>> => {
    return http.get<PaginationResult<CartItem>>('/api/cart', params);
  },

  // 添加商品到购物车
  addToCart: (data: { productId: number; quantity: number }): Promise<CartItem> => {
    return http.post<CartItem>('/api/cart', data);
  },

  // 更新购物车商品数量
  updateCartItem: (id: number, quantity: number): Promise<CartItem> => {
    return http.put<CartItem>(`/api/cart/${id}`, { quantity });
  },

  // 从购物车删除商品
  removeFromCart: (id: number): Promise<void> => {
    return http.delete(`/api/cart/${id}`);
  },

  // 批量删除购物车商品
  batchRemoveFromCart: (ids: number[]): Promise<void> => {
    return http.post('/api/cart/batch-delete', { ids });
  },

  // 清空购物车
  clearCart: (): Promise<void> => {
    return http.delete('/api/cart/clear');
  },

  // 选择/取消选择商品
  toggleSelectItem: (id: number, selected: boolean): Promise<void> => {
    return http.patch(`/api/cart/${id}/select`, { selected });
  },

  // 全选/取消全选
  toggleSelectAll: (selected: boolean): Promise<void> => {
    return http.patch('/api/cart/select-all', { selected });
  },

  // 获取购物车统计
  getCartStats: (): Promise<{ total: number; itemCount: number; selectedTotal: number }> => {
    return http.get('/api/cart/stats');
  },

  // 同步本地购物车到服务器
  syncCart: (items: Array<{ productId: number; quantity: number }>): Promise<CartItem[]> => {
    return http.post<CartItem[]>('/api/cart/sync', { items });
  },
};