import { useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { authApi } from '@/api';
import { useAuthStore } from '@/store';
import { QUERY_KEYS } from '@/config/queryClient';

/**
 * 认证相关自定义hook
 */
const useAuth = () => {
  const { token, user, isAuthenticated, login, logout, updateUser } = useAuthStore();

  // 自动获取当前用户信息
  const {
    data: currentUser,
    isLoading,
    error,
    refetch,
  } = useQuery({
    queryKey: [QUERY_KEYS.CURRENT_USER],
    queryFn: authApi.getCurrentUser,
    enabled: !!token && !user, // 有token但没有用户信息时才获取
  });

  // 当获取到用户信息时更新store
  useEffect(() => {
    if (currentUser && !user) {
      updateUser(currentUser);
    }
  }, [currentUser, user, updateUser]);

  // 检查token是否有效
  const checkToken = async (): Promise<boolean> => {
    if (!token) return false;

    try {
      await authApi.verifyToken(token);
      return true;
    } catch (error) {
      return false;
    }
  };

  // 刷新token
  const refreshToken = async (): Promise<boolean> => {
    try {
      const data = await authApi.refreshToken();
      login(data);
      return true;
    } catch (error) {
      logout();
      return false;
    }
  };

  // 检查用户是否有特定角色
  const hasRole = (role: string): boolean => {
    return user?.role === role;
  };

  // 检查用户是否有任意角色
  const hasAnyRole = (roles: string[]): boolean => {
    return roles.includes(user?.role || '');
  };

  // 检查用户是否没有特定角色
  const hasNoRole = (role: string): boolean => {
    return user?.role !== role;
  };

  // 检查用户是否没有任何角色
  const hasNoRoles = (roles: string[]): boolean => {
    return !roles.includes(user?.role || '');
  };

  return {
    // 状态
    token,
    user,
    isAuthenticated,
    isLoading,
    error,

    // 操作
    login,
    logout,
    updateUser,
    refetch,

    // 工具方法
    checkToken,
    refreshToken,
    hasRole,
    hasAnyRole,
    hasNoRole,
    hasNoRoles,

    // 快捷属性
    isAdmin: hasRole('ADMIN'),
    isSupplier: hasRole('SUPPLIER'),
    isUser: hasRole('USER'),
  };
};

export default useAuth;