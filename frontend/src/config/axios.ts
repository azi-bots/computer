import axios from 'axios';
import type { InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';
import { message } from 'antd';
import { API_CONFIG, STORAGE_KEYS } from './constants';
import type { ApiResponse } from '@/types';

// 创建axios实例
const axiosInstance = axios.create({
  baseURL: API_CONFIG.BASE_URL,
  timeout: API_CONFIG.TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 从localStorage获取token
    const token = localStorage.getItem(STORAGE_KEYS.TOKEN);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    // 添加请求时间戳防止缓存
    if (config.method?.toLowerCase() === 'get') {
      config.params = {
        ...config.params,
        _t: Date.now(),
      };
    }

    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const { data } = response;

    // 如果返回的是文件流，直接返回
    if (response.config.responseType === 'blob') {
      return response;
    }

    // 检查业务状态码
    if (data.code !== 200 && data.code !== 0) {
      // 业务错误
      message.error(data.message || '请求失败');
      return Promise.reject(new Error(data.message || '请求失败'));
    }

    // 返回数据部分
    return data.data !== undefined ? data.data : data;
  },
  (error: AxiosError) => {
    // 处理HTTP错误
    if (error.response) {
      const { status, data } = error.response;

      switch (status) {
        case 401:
          // 未授权，清除token并跳转到登录页
          localStorage.removeItem(STORAGE_KEYS.TOKEN);
          localStorage.removeItem(STORAGE_KEYS.USER);
          message.error('登录已过期，请重新登录');
          // 延迟跳转，避免message被立即清除
          setTimeout(() => {
            window.location.href = '/login';
          }, 1500);
          break;
        case 403:
          message.error('权限不足，无法访问');
          break;
        case 404:
          message.error('请求的资源不存在');
          break;
        case 500:
          message.error('服务器内部错误');
          break;
        default:
          message.error(`请求失败: ${status}`);
      }

      // 如果有业务错误消息，显示它
      if (data && typeof data === 'object' && 'message' in data) {
        message.error((data as any).message);
      }
    } else if (error.request) {
      // 请求未收到响应
      message.error('网络错误，请检查网络连接');
    } else {
      // 请求配置错误
      message.error('请求配置错误');
    }

    return Promise.reject(error);
  }
);

// 封装常用的HTTP方法
export const http = {
  get: <T = any>(url: string, params?: any): Promise<T> => {
    return axiosInstance.get(url, { params });
  },
  post: <T = any>(url: string, data?: any): Promise<T> => {
    return axiosInstance.post(url, data);
  },
  put: <T = any>(url: string, data?: any): Promise<T> => {
    return axiosInstance.put(url, data);
  },
  delete: <T = any>(url: string, params?: any): Promise<T> => {
    return axiosInstance.delete(url, { params });
  },
  patch: <T = any>(url: string, data?: any): Promise<T> => {
    return axiosInstance.patch(url, data);
  },
  // 文件上传
  upload: <T = any>(url: string, file: File, onProgress?: (progress: number) => void): Promise<T> => {
    const formData = new FormData();
    formData.append('file', file);

    return axiosInstance.post(url, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      onUploadProgress: (progressEvent) => {
        if (progressEvent.total && onProgress) {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          onProgress(progress);
        }
      },
    });
  },
  // 文件下载
  download: (url: string, params?: any, filename?: string): Promise<void> => {
    return axiosInstance.get(url, {
      params,
      responseType: 'blob',
    }).then((response) => {
      const blob = new Blob([response.data]);
      const downloadUrl = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = downloadUrl;
      link.download = filename || 'download';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(downloadUrl);
    });
  },
};

export default axiosInstance;