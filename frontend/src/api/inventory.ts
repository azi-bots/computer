import { http } from '@/config/axios';
import type { ApiResponse, Inventory, PaginationParams, PaginationResult } from '@/types';

// 库存相关API
export const inventoryApi = {
  // 获取库存列表
  getInventories: (params?: PaginationParams & {
    productId?: number;
    status?: string;
    warehouse?: string;
  }): Promise<PaginationResult<Inventory>> => {
    return http.get<PaginationResult<Inventory>>('/api/inventories', params);
  },

  // 获取库存详情
  getInventory: (id: number): Promise<Inventory> => {
    return http.get<Inventory>(`/api/inventories/${id}`);
  },

  // 获取商品库存
  getInventoryByProduct: (productId: number): Promise<Inventory> => {
    return http.get<Inventory>(`/api/inventories/product/${productId}`);
  },

  // 更新库存
  updateInventory: (id: number, data: Partial<Inventory>): Promise<Inventory> => {
    return http.put<Inventory>(`/api/inventories/${id}`, data);
  },

  // 调整库存
  adjustInventory: (data: {
    productId: number;
    quantity: number;
    type: 'IN' | 'OUT' | 'ADJUST';
    reason: string;
    remark?: string;
  }): Promise<Inventory> => {
    return http.post<Inventory>('/api/inventories/adjust', data);
  },

  // 批量更新库存
  batchUpdateInventory: (items: Array<{
    productId: number;
    quantity: number;
    type: 'IN' | 'OUT' | 'ADJUST';
  }>): Promise<void> => {
    return http.post('/api/inventories/batch-adjust', { items });
  },

  // 获取库存统计
  getInventoryStats: (): Promise<{
    totalProducts: number;
    totalQuantity: number;
    lowStockCount: number;
    outOfStockCount: number;
  }> => {
    return http.get('/api/inventories/stats');
  },

  // 获取库存变化记录
  getInventoryHistory: (productId: number, params?: PaginationParams): Promise<PaginationResult<any>> => {
    return http.get<PaginationResult<any>>(`/api/inventories/${productId}/history`, params);
  },

  // 导出库存
  exportInventory: (): Promise<Blob> => {
    return http.get('/api/inventories/export', {}, '库存导出.xlsx');
  },
};