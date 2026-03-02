import { http } from '@/config/axios';
import type { ApiResponse, Payment, PaginationParams, PaginationResult } from '@/types';

// 支付相关API
export const paymentApi = {
  // 获取支付记录
  getPayments: (params?: PaginationParams & {
    orderId?: number;
    status?: string;
    method?: string;
  }): Promise<PaginationResult<Payment>> => {
    return http.get<PaginationResult<Payment>>('/api/payments', params);
  },

  // 获取支付详情
  getPayment: (id: number): Promise<Payment> => {
    return http.get<Payment>(`/api/payments/${id}`);
  },

  // 创建支付
  createPayment: (data: {
    orderId: number;
    method: string;
    amount: number;
  }): Promise<Payment> => {
    return http.post<Payment>('/api/payments', data);
  },

  // 更新支付状态
  updatePaymentStatus: (id: number, status: string, transactionId?: string): Promise<void> => {
    return http.patch(`/api/payments/${id}/status`, { status, transactionId });
  },

  // 获取订单支付记录
  getOrderPayments: (orderId: number): Promise<Payment[]> => {
    return http.get<Payment[]>(`/api/payments/order/${orderId}`);
  },

  // 模拟支付（开发环境）
  simulatePayment: (paymentId: number, success: boolean = true): Promise<void> => {
    return http.post(`/api/payments/${paymentId}/simulate`, { success });
  },

  // 获取支付统计
  getPaymentStats: (): Promise<{
    totalAmount: number;
    successCount: number;
    pendingCount: number;
    failedCount: number;
    todayAmount: number;
  }> => {
    return http.get('/api/payments/stats');
  },

  // 退款
  refund: (paymentId: number, amount: number, reason: string): Promise<void> => {
    return http.post(`/api/payments/${paymentId}/refund`, { amount, reason });
  },
};