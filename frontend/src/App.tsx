import { Suspense, lazy } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Spin } from 'antd';
import { useAuthStore } from './store';
import { ROUTE_PATHS } from './config/constants';
import { hasRoutePermission } from './config/routes';

// 懒加载页面组件
const HomePage = lazy(() => import('@/pages/home/HomePage'));
const LoginPage = lazy(() => import('@/pages/auth/LoginPage'));
const RegisterPage = lazy(() => import('@/pages/auth/RegisterPage'));
const ProductsPage = lazy(() => import('@/pages/product/ProductsPage'));
const ProductDetailPage = lazy(() => import('@/pages/product/ProductDetailPage'));
const CartPage = lazy(() => import('@/pages/cart/CartPage'));
const OrdersPage = lazy(() => import('@/pages/order/OrdersPage'));
const OrderDetailPage = lazy(() => import('@/pages/order/OrderDetailPage'));
const ProfilePage = lazy(() => import('@/pages/user/ProfilePage'));
const AddressPage = lazy(() => import('@/pages/user/AddressPage'));

// 管理页面
const AdminDashboard = lazy(() => import('@/pages/admin/Dashboard'));
const AdminProducts = lazy(() => import('@/pages/admin/Products'));
const AdminOrders = lazy(() => import('@/pages/admin/Orders'));
const AdminUsers = lazy(() => import('@/pages/admin/Users'));
const AdminCategories = lazy(() => import('@/pages/admin/Categories'));

// 供应商页面
const SupplierDashboard = lazy(() => import('@/pages/supplier/Dashboard'));
const SupplierInventory = lazy(() => import('@/pages/supplier/Inventory'));
const SupplierOrders = lazy(() => import('@/pages/supplier/Orders'));
const SupplierProducts = lazy(() => import('@/pages/supplier/Products'));

// 加载中组件
const LoadingFallback = () => (
  <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
    <Spin size="large" />
  </div>
);

// 路由守卫组件
interface ProtectedRouteProps {
  children: React.ReactNode;
  path: string;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, path }) => {
  const { user, isAuthenticated } = useAuthStore();
  const { hasPermission } = hasRoutePermission(path, user?.role);

  if (!hasPermission) {
    // 如果没有权限，重定向到登录页或首页
    return <Navigate to={isAuthenticated ? ROUTE_PATHS.HOME : ROUTE_PATHS.LOGIN} replace />;
  }

  return <>{children}</>;
};

function App() {
  return (
    <Router>
      <Suspense fallback={<LoadingFallback />}>
        <Routes>
          {/* 公共路由 */}
          <Route path={ROUTE_PATHS.HOME} element={<HomePage />} />
          <Route path={ROUTE_PATHS.LOGIN} element={<LoginPage />} />
          <Route path={ROUTE_PATHS.REGISTER} element={<RegisterPage />} />
          <Route path={ROUTE_PATHS.PRODUCTS} element={<ProductsPage />} />
          <Route path={ROUTE_PATHS.PRODUCT_DETAIL} element={<ProductDetailPage />} />
          <Route path={ROUTE_PATHS.CATEGORIES} element={<div>分类页面</div>} />

          {/* 用户路由（需要认证） */}
          <Route
            path={ROUTE_PATHS.CART}
            element={
              <ProtectedRoute path={ROUTE_PATHS.CART}>
                <CartPage />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.CHECKOUT}
            element={
              <ProtectedRoute path={ROUTE_PATHS.CHECKOUT}>
                <div>结算页面</div>
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.ORDERS}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ORDERS}>
                <OrdersPage />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.ORDER_DETAIL}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ORDER_DETAIL}>
                <OrderDetailPage />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.PROFILE}
            element={
              <ProtectedRoute path={ROUTE_PATHS.PROFILE}>
                <ProfilePage />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.ADDRESSES}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ADDRESSES}>
                <AddressPage />
              </ProtectedRoute>
            }
          />

          {/* 管理路由（需要管理员角色） */}
          <Route
            path={ROUTE_PATHS.ADMIN}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ADMIN}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.ADMIN_DASHBOARD}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ADMIN_DASHBOARD}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.ADMIN_PRODUCTS}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ADMIN_PRODUCTS}>
                <AdminProducts />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.ADMIN_ORDERS}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ADMIN_ORDERS}>
                <AdminOrders />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.ADMIN_USERS}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ADMIN_USERS}>
                <AdminUsers />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.ADMIN_CATEGORIES}
            element={
              <ProtectedRoute path={ROUTE_PATHS.ADMIN_CATEGORIES}>
                <AdminCategories />
              </ProtectedRoute>
            }
          />

          {/* 供应商路由（需要供应商角色） */}
          <Route
            path={ROUTE_PATHS.SUPPLIER}
            element={
              <ProtectedRoute path={ROUTE_PATHS.SUPPLIER}>
                <SupplierDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.SUPPLIER_DASHBOARD}
            element={
              <ProtectedRoute path={ROUTE_PATHS.SUPPLIER_DASHBOARD}>
                <SupplierDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.SUPPLIER_INVENTORY}
            element={
              <ProtectedRoute path={ROUTE_PATHS.SUPPLIER_INVENTORY}>
                <SupplierInventory />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.SUPPLIER_ORDERS}
            element={
              <ProtectedRoute path={ROUTE_PATHS.SUPPLIER_ORDERS}>
                <SupplierOrders />
              </ProtectedRoute>
            }
          />
          <Route
            path={ROUTE_PATHS.SUPPLIER_PRODUCTS}
            element={
              <ProtectedRoute path={ROUTE_PATHS.SUPPLIER_PRODUCTS}>
                <SupplierProducts />
              </ProtectedRoute>
            }
          />

          {/* 404路由 */}
          <Route path="*" element={<Navigate to={ROUTE_PATHS.HOME} replace />} />
        </Routes>
      </Suspense>
    </Router>
  );
}

export default App;