import { http } from '@/config/axios';
import type { ApiResponse, Order, OrderItem, PaginationParams, PaginationResult } from '@/types';

// 订单相关API
export const orderApi = {
  // 获取订单列表
  getOrders: (params?: PaginationParams & {
    status?: string;
    startDate?: string;
    endDate?: string;
    keyword?: string;
  }): Promise<PaginationResult<Order>> => {
    return http.get<PaginationResult<Order>>('/api/orders', params);
  },

  // 获取用户订单
  getUserOrders: (params?: PaginationParams): Promise<PaginationResult<Order>> => {
    return http.get<PaginationResult<Order>>('/api/orders/my', params);
  },

  // 获取供应商订单
  getSupplierOrders: (params?: PaginationParams): Promise<PaginationResult<Order>> => {
    return http.get<PaginationResult<Order>>('/api/orders/supplier', params);
  },

  // 获取订单详情
  getOrder: (id: number): Promise<Order> => {
    return http.get<Order>(`/api/orders/${id}`);
  },

  // 创建订单
  createOrder: (data: {
    items: Array<{ productId: number; quantity: number }>;
    shippingAddressId: number;
    paymentMethod: string;
    remark?: string;
  }): Promise<Order> => {
    return http.post<Order>('/api/orders', data);
  },

  // 更新订单
  updateOrder: (id: number, data: Partial<Order>): Promise<Order> => {
    return http.put<Order>(`/api/orders/${id}`, data);
  },

  // 取消订单
  cancelOrder: (id: number): Promise<void> => {
    return http.post(`/api/orders/${id}/cancel`);
  },

  // 确认收货
  confirmReceipt: (id: number): Promise<void> => {
    return http.post(`/api/orders/${id}/confirm-receipt`);
  },

  // 获取订单商品项
  getOrderItems: (orderId: number): Promise<OrderItem[]> => {
    return http.get<OrderItem[]>(`/api/orders/${orderId}/items`);
  },

  // 更新订单状态（管理员/供应商）
  updateOrderStatus: (id: number, status: string): Promise<void> => {
    return http.patch(`/api/orders/${id}/status`, { status });
  },

  // 获取订单统计
  getOrderStats: (): Promise<{
    total: number;
    pending: number;
    paid: number;
    shipped: number;
    delivered: number;
    cancelled: number;
  }> => {
    return http.get('/api/orders/stats');
  },

  // 导出订单
  exportOrders: (params?: {
    startDate?: string;
    endDate?: string;
    status?: string;
  }): Promise<Blob> => {
    return http.get('/api/orders/export', params, '订单导出.xlsx');
  },

  // 批量更新订单状态
  batchUpdateOrderStatus: (ids: number[], status: string): Promise<void> => {
    return http.post('/api/orders/batch-update-status', { ids, status });
  },
};