import { http } from '@/config/axios';
import type { ApiResponse, Category, PaginationParams, PaginationResult } from '@/types';

// 分类相关API
export const categoryApi = {
  // 获取分类列表
  getCategories: (params?: PaginationParams): Promise<PaginationResult<Category>> => {
    return http.get<PaginationResult<Category>>('/api/categories', params);
  },

  // 获取所有分类（树形结构）
  getCategoryTree: (): Promise<Category[]> => {
    return http.get<Category[]>('/api/categories/tree');
  },

  // 获取分类详情
  getCategory: (id: number): Promise<Category> => {
    return http.get<Category>(`/api/categories/${id}`);
  },

  // 创建分类
  createCategory: (data: Omit<Category, 'id'>): Promise<Category> => {
    return http.post<Category>('/api/categories', data);
  },

  // 更新分类
  updateCategory: (id: number, data: Partial<Category>): Promise<Category> => {
    return http.put<Category>(`/api/categories/${id}`, data);
  },

  // 删除分类
  deleteCategory: (id: number): Promise<void> => {
    return http.delete(`/api/categories/${id}`);
  },

  // 获取子分类
  getChildCategories: (parentId: number): Promise<Category[]> => {
    return http.get<Category[]>(`/api/categories/parent/${parentId}`);
  },

  // 更新分类排序
  updateCategoryOrder: (ids: number[]): Promise<void> => {
    return http.put('/api/categories/order', { ids });
  },

  // 更新分类状态
  updateCategoryStatus: (id: number, status: string): Promise<void> => {
    return http.patch(`/api/categories/${id}/status`, { status });
  },
};