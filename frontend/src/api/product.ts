import { http } from '@/config/axios';
import type { ApiResponse, PaginationParams, PaginationResult, Product } from '@/types';

// 商品相关API
export const productApi = {
  // 获取商品列表（分页）
  getProducts: (params?: PaginationParams & {
    categoryId?: number;
    supplierId?: number;
    status?: string;
    keyword?: string;
    minPrice?: number;
    maxPrice?: number;
  }): Promise<PaginationResult<Product>> => {
    return http.get<PaginationResult<Product>>('/api/products', params);
  },

  // 获取商品详情
  getProduct: (id: number): Promise<Product> => {
    return http.get<Product>(`/api/products/${id}`);
  },

  // 创建商品（管理员/供应商）
  createProduct: (data: Omit<Product, 'id' | 'createdAt' | 'updatedAt'>): Promise<Product> => {
    return http.post<Product>('/api/products', data);
  },

  // 更新商品
  updateProduct: (id: number, data: Partial<Product>): Promise<Product> => {
    return http.put<Product>(`/api/products/${id}`, data);
  },

  // 删除商品
  deleteProduct: (id: number): Promise<void> => {
    return http.delete(`/api/products/${id}`);
  },

  // 批量删除商品
  batchDeleteProducts: (ids: number[]): Promise<void> => {
    return http.post('/api/products/batch-delete', { ids });
  },

  // 更新商品状态
  updateProductStatus: (id: number, status: string): Promise<void> => {
    return http.patch(`/api/products/${id}/status`, { status });
  },

  // 获取分类商品
  getProductsByCategory: (categoryId: number, params?: PaginationParams): Promise<PaginationResult<Product>> => {
    return http.get<PaginationResult<Product>>(`/api/products/category/${categoryId}`, params);
  },

  // 获取供应商商品
  getProductsBySupplier: (supplierId: number, params?: PaginationParams): Promise<PaginationResult<Product>> => {
    return http.get<PaginationResult<Product>>(`/api/products/supplier/${supplierId}`, params);
  },

  // 搜索商品
  searchProducts: (keyword: string, params?: PaginationParams): Promise<PaginationResult<Product>> => {
    return http.get<PaginationResult<Product>>('/api/products/search', { keyword, ...params });
  },

  // 获取热门商品
  getHotProducts: (limit: number = 10): Promise<Product[]> => {
    return http.get<Product[]>('/api/products/hot', { limit });
  },

  // 获取新品
  getNewProducts: (limit: number = 10): Promise<Product[]> => {
    return http.get<Product[]>('/api/products/new', { limit });
  },

  // 上传商品图片
  uploadProductImage: (productId: number, file: File, onProgress?: (progress: number) => void): Promise<{ url: string }> => {
    return http.upload<{ url: string }>(`/api/products/${productId}/upload-image`, file, onProgress);
  },

  // 删除商品图片
  deleteProductImage: (productId: number, imageUrl: string): Promise<void> => {
    return http.delete(`/api/products/${productId}/images`, { imageUrl });
  },
};