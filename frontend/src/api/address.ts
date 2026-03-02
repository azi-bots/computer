import { http } from '@/config/axios';
import type { ApiResponse, UserAddress } from '@/types';

// 地址相关API
export const addressApi = {
  // 获取用户地址列表
  getAddresses: (): Promise<UserAddress[]> => {
    return http.get<UserAddress[]>('/api/addresses');
  },

  // 获取地址详情
  getAddress: (id: number): Promise<UserAddress> => {
    return http.get<UserAddress>(`/api/addresses/${id}`);
  },

  // 添加地址
  addAddress: (data: Omit<UserAddress, 'id' | 'userId' | 'createdAt' | 'updatedAt'>): Promise<UserAddress> => {
    return http.post<UserAddress>('/api/addresses', data);
  },

  // 更新地址
  updateAddress: (id: number, data: Partial<UserAddress>): Promise<UserAddress> => {
    return http.put<UserAddress>(`/api/addresses/${id}`, data);
  },

  // 删除地址
  deleteAddress: (id: number): Promise<void> => {
    return http.delete(`/api/addresses/${id}`);
  },

  // 设置默认地址
  setDefaultAddress: (id: number): Promise<void> => {
    return http.patch(`/api/addresses/${id}/default`);
  },

  // 获取默认地址
  getDefaultAddress: (): Promise<UserAddress | null> => {
    return http.get<UserAddress | null>('/api/addresses/default');
  },
};