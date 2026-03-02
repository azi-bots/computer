import { http } from '@/config/axios';
import type { ApiResponse, User, PaginationParams, PaginationResult } from '@/types';

// 用户相关API
export const userApi = {
  // 获取用户列表（管理员）
  getUsers: (params?: PaginationParams & {
    role?: string;
    status?: string;
    keyword?: string;
  }): Promise<PaginationResult<User>> => {
    return http.get<PaginationResult<User>>('/api/users', params);
  },

  // 获取用户详情
  getUser: (id: number): Promise<User> => {
    return http.get<User>(`/api/users/${id}`);
  },

  // 更新用户信息
  updateUser: (id: number, data: Partial<User>): Promise<User> => {
    return http.put<User>(`/api/users/${id}`, data);
  },

  // 删除用户
  deleteUser: (id: number): Promise<void> => {
    return http.delete(`/api/users/${id}`);
  },

  // 更新用户状态
  updateUserStatus: (id: number, status: string): Promise<void> => {
    return http.patch(`/api/users/${id}/status`, { status });
  },

  // 更新用户角色
  updateUserRole: (id: number, role: string): Promise<void> => {
    return http.patch(`/api/users/${id}/role`, { role });
  },

  // 获取用户统计
  getUserStats: (): Promise<{
    total: number;
    active: number;
    inactive: number;
    banned: number;
    todayNew: number;
  }> => {
    return http.get('/api/users/stats');
  },

  // 批量操作用户
  batchUpdateUsers: (ids: number[], data: { status?: string; role?: string }): Promise<void> => {
    return http.post('/api/users/batch-update', { ids, ...data });
  },

  // 搜索用户
  searchUsers: (keyword: string, params?: PaginationParams): Promise<PaginationResult<User>> => {
    return http.get<PaginationResult<User>>('/api/users/search', { keyword, ...params });
  },
};