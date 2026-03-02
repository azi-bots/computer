import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { STORAGE_KEYS } from '@/config/constants';
import type { AuthResponse, User } from '@/types';

interface AuthState {
  token: string | null;
  user: User | null;
  isAuthenticated: boolean;
  loading: boolean;
}

interface AuthActions {
  setToken: (token: string) => void;
  setUser: (user: User) => void;
  login: (authData: AuthResponse) => void;
  logout: () => void;
  updateUser: (user: Partial<User>) => void;
  clear: () => void;
}

const initialState: AuthState = {
  token: null,
  user: null,
  isAuthenticated: false,
  loading: false,
};

export const useAuthStore = create<AuthState & AuthActions>()(
  persist(
    (set, get) => ({
      ...initialState,

      setToken: (token: string) => {
        set({ token, isAuthenticated: true });
        localStorage.setItem(STORAGE_KEYS.TOKEN, token);
      },

      setUser: (user: User) => {
        set({ user });
        localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(user));
      },

      login: (authData: AuthResponse) => {
        const { token, user } = authData;
        set({
          token,
          user,
          isAuthenticated: true,
          loading: false,
        });
        localStorage.setItem(STORAGE_KEYS.TOKEN, token);
        localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(user));
      },

      logout: () => {
        set(initialState);
        localStorage.removeItem(STORAGE_KEYS.TOKEN);
        localStorage.removeItem(STORAGE_KEYS.USER);
        // 清除其他相关存储
        localStorage.removeItem(STORAGE_KEYS.CART);
      },

      updateUser: (updatedUser: Partial<User>) => {
        const currentUser = get().user;
        if (currentUser) {
          const newUser = { ...currentUser, ...updatedUser };
          set({ user: newUser });
          localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(newUser));
        }
      },

      clear: () => {
        set(initialState);
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        token: state.token,
        user: state.user,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
);

// 导出hooks
export const useToken = () => useAuthStore((state) => state.token);
export const useUser = () => useAuthStore((state) => state.user);
export const useIsAuthenticated = () => useAuthStore((state) => state.isAuthenticated);
export const useAuthLoading = () => useAuthStore((state) => state.loading);
export const useAuthActions = () => {
  const { setToken, setUser, login, logout, updateUser, clear } = useAuthStore();
  return { setToken, setUser, login, logout, updateUser, clear };
};