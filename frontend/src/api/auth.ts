import { http } from '@/config/axios';
import type { ApiResponse, AuthResponse, LoginRequest, RegisterRequest, User } from '@/types';

// 认证相关API
export const authApi = {
  // 登录
  login: (data: LoginRequest): Promise<AuthResponse> => {
    return http.post<AuthResponse>('/api/auth/login', data);
  },

  // 注册
  register: (data: RegisterRequest): Promise<AuthResponse> => {
    return http.post<AuthResponse>('/api/auth/register', data);
  },

  // 退出登录
  logout: (): Promise<void> => {
    return http.post('/api/auth/logout');
  },

  // 刷新token
  refreshToken: (): Promise<AuthResponse> => {
    return http.post<AuthResponse>('/api/auth/refresh');
  },

  // 获取当前用户信息
  getCurrentUser: (): Promise<User> => {
    return http.get<User>('/api/auth/me');
  },

  // 修改密码
  changePassword: (data: { oldPassword: string; newPassword: string }): Promise<void> => {
    return http.put('/api/auth/change-password', data);
  },

  // 重置密码请求
  requestPasswordReset: (email: string): Promise<void> => {
    return http.post('/api/auth/reset-password/request', { email });
  },

  // 重置密码确认
  resetPassword: (data: { token: string; newPassword: string }): Promise<void> => {
    return http.post('/api/auth/reset-password/confirm', data);
  },

  // 验证token
  verifyToken: (token: string): Promise<{ valid: boolean }> => {
    return http.get(`/api/auth/verify?token=${token}`);
  },
};