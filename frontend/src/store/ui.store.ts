import { create } from 'zustand';
import type { Notification } from '@/types';

interface UIState {
  // 加载状态
  loading: boolean;
  loadingText: string;

  // 主题
  theme: 'light' | 'dark';

  // 布局
  sidebarCollapsed: boolean;
  mobileMenuOpen: boolean;

  // 通知
  notifications: Notification[];

  // 模态框
  modals: Record<string, boolean>;
}

interface UIActions {
  // 加载状态
  setLoading: (loading: boolean, text?: string) => void;

  // 主题
  setTheme: (theme: 'light' | 'dark') => void;
  toggleTheme: () => void;

  // 布局
  setSidebarCollapsed: (collapsed: boolean) => void;
  toggleSidebar: () => void;
  setMobileMenuOpen: (open: boolean) => void;
  toggleMobileMenu: () => void;

  // 通知
  addNotification: (notification: Omit<Notification, 'id'>) => void;
  removeNotification: (id: string) => void;
  clearNotifications: () => void;

  // 模态框
  openModal: (modalId: string) => void;
  closeModal: (modalId: string) => void;
  toggleModal: (modalId: string) => void;

  // 重置
  reset: () => void;
}

const initialState: UIState = {
  loading: false,
  loadingText: '加载中...',
  theme: 'light',
  sidebarCollapsed: false,
  mobileMenuOpen: false,
  notifications: [],
  modals: {},
};

export const useUIStore = create<UIState & UIActions>()((set, get) => ({
  ...initialState,

  setLoading: (loading: boolean, text: string = '加载中...') => {
    set({ loading, loadingText: text });
  },

  setTheme: (theme: 'light' | 'dark') => {
    set({ theme });
    // 保存到localStorage
    localStorage.setItem('theme', theme);
    // 更新HTML class
    document.documentElement.setAttribute('data-theme', theme);
  },

  toggleTheme: () => {
    const { theme } = get();
    const newTheme = theme === 'light' ? 'dark' : 'light';
    set({ theme: newTheme });
    localStorage.setItem('theme', newTheme);
    document.documentElement.setAttribute('data-theme', newTheme);
  },

  setSidebarCollapsed: (collapsed: boolean) => {
    set({ sidebarCollapsed: collapsed });
  },

  toggleSidebar: () => {
    const { sidebarCollapsed } = get();
    set({ sidebarCollapsed: !sidebarCollapsed });
  },

  setMobileMenuOpen: (open: boolean) => {
    set({ mobileMenuOpen: open });
  },

  toggleMobileMenu: () => {
    const { mobileMenuOpen } = get();
    set({ mobileMenuOpen: !mobileMenuOpen });
  },

  addNotification: (notification: Omit<Notification, 'id'>) => {
    const id = Date.now().toString() + Math.random().toString(36).substr(2, 9);
    const newNotification: Notification = {
      id,
      duration: 5000,
      ...notification,
    };

    set((state) => ({
      notifications: [...state.notifications, newNotification],
    }));

    // 自动移除
    if (newNotification.duration && newNotification.duration > 0) {
      setTimeout(() => {
        get().removeNotification(id);
      }, newNotification.duration);
    }
  },

  removeNotification: (id: string) => {
    set((state) => ({
      notifications: state.notifications.filter((n) => n.id !== id),
    }));
  },

  clearNotifications: () => {
    set({ notifications: [] });
  },

  openModal: (modalId: string) => {
    set((state) => ({
      modals: {
        ...state.modals,
        [modalId]: true,
      },
    }));
  },

  closeModal: (modalId: string) => {
    set((state) => ({
      modals: {
        ...state.modals,
        [modalId]: false,
      },
    }));
  },

  toggleModal: (modalId: string) => {
    const { modals } = get();
    set({
      modals: {
        ...modals,
        [modalId]: !modals[modalId],
      },
    });
  },

  reset: () => {
    set(initialState);
  },
}));

// 导出hooks
export const useLoading = () => useUIStore((state) => state.loading);
export const useLoadingText = () => useUIStore((state) => state.loadingText);
export const useTheme = () => useUIStore((state) => state.theme);
export const useSidebarCollapsed = () => useUIStore((state) => state.sidebarCollapsed);
export const useMobileMenuOpen = () => useUIStore((state) => state.mobileMenuOpen);
export const useNotifications = () => useUIStore((state) => state.notifications);
export const useModals = () => useUIStore((state) => state.modals);

// 导出actions hooks
export const useUIActions = () => {
  const {
    setLoading,
    setTheme,
    toggleTheme,
    setSidebarCollapsed,
    toggleSidebar,
    setMobileMenuOpen,
    toggleMobileMenu,
    addNotification,
    removeNotification,
    clearNotifications,
    openModal,
    closeModal,
    toggleModal,
    reset,
  } = useUIStore();

  return {
    setLoading,
    setTheme,
    toggleTheme,
    setSidebarCollapsed,
    toggleSidebar,
    setMobileMenuOpen,
    toggleMobileMenu,
    addNotification,
    removeNotification,
    clearNotifications,
    openModal,
    closeModal,
    toggleModal,
    reset,
  };
};