import React from 'react';
import { Layout, Menu } from 'antd';
import {
  DashboardOutlined,
  ShoppingOutlined,
  ShoppingCartOutlined,
  OrderedListOutlined,
  UserOutlined,
  TeamOutlined,
  AppstoreOutlined,
  BankOutlined,
  ContainerOutlined,
  BarChartOutlined,
  SettingOutlined,
} from '@ant-design/icons';
import { useNavigate, useLocation } from 'react-router-dom';
import { useUIStore, useAuthStore } from '@/store';
import { ROUTE_PATHS, USER_ROLES } from '@/config/constants';
import type { MenuProps } from 'antd';

const { Sider } = Layout;

interface SidebarProps {
  mode?: 'admin' | 'supplier' | 'user';
}

const Sidebar: React.FC<SidebarProps> = ({ mode = 'user' }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { sidebarCollapsed } = useUIStore();
  const { user } = useAuthStore();

  // 用户侧边栏菜单
  const userMenuItems: MenuProps['items'] = [
    {
      key: 'dashboard',
      icon: <DashboardOutlined />,
      label: '仪表盘',
      onClick: () => navigate(ROUTE_PATHS.HOME),
    },
    {
      key: 'products',
      icon: <ShoppingOutlined />,
      label: '商品',
      children: [
        {
          key: 'all-products',
          label: '全部商品',
          onClick: () => navigate(ROUTE_PATHS.PRODUCTS),
        },
        {
          key: 'categories',
          label: '分类浏览',
          onClick: () => navigate(ROUTE_PATHS.CATEGORIES),
        },
      ],
    },
    {
      key: 'cart',
      icon: <ShoppingCartOutlined />,
      label: '购物车',
      onClick: () => navigate(ROUTE_PATHS.CART),
    },
    {
      key: 'orders',
      icon: <OrderedListOutlined />,
      label: '我的订单',
      onClick: () => navigate(ROUTE_PATHS.ORDERS),
    },
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心',
      children: [
        {
          key: 'profile-info',
          label: '个人信息',
          onClick: () => navigate(ROUTE_PATHS.PROFILE),
        },
        {
          key: 'addresses',
          label: '地址管理',
          onClick: () => navigate(ROUTE_PATHS.ADDRESSES),
        },
        {
          key: 'security',
          label: '账户安全',
          onClick: () => navigate(`${ROUTE_PATHS.PROFILE}/security`),
        },
      ],
    },
  ];

  // 管理后台侧边栏菜单
  const adminMenuItems: MenuProps['items'] = [
    {
      key: 'admin-dashboard',
      icon: <DashboardOutlined />,
      label: '仪表盘',
      onClick: () => navigate(ROUTE_PATHS.ADMIN_DASHBOARD),
    },
    {
      key: 'products-management',
      icon: <ShoppingOutlined />,
      label: '商品管理',
      children: [
        {
          key: 'all-products-admin',
          label: '所有商品',
          onClick: () => navigate(ROUTE_PATHS.ADMIN_PRODUCTS),
        },
        {
          key: 'add-product',
          label: '添加商品',
          onClick: () => navigate(`${ROUTE_PATHS.ADMIN_PRODUCTS}/add`),
        },
        {
          key: 'categories-management',
          label: '分类管理',
          onClick: () => navigate(ROUTE_PATHS.ADMIN_CATEGORIES),
        },
      ],
    },
    {
      key: 'orders-management',
      icon: <OrderedListOutlined />,
      label: '订单管理',
      onClick: () => navigate(ROUTE_PATHS.ADMIN_ORDERS),
    },
    {
      key: 'users-management',
      icon: <TeamOutlined />,
      label: '用户管理',
      onClick: () => navigate(ROUTE_PATHS.ADMIN_USERS),
    },
    {
      key: 'inventory',
      icon: <ContainerOutlined />,
      label: '库存管理',
      onClick: () => navigate(`${ROUTE_PATHS.ADMIN}/inventory`),
    },
    {
      key: 'statistics',
      icon: <BarChartOutlined />,
      label: '数据统计',
      children: [
        {
          key: 'sales-stats',
          label: '销售统计',
          onClick: () => navigate(`${ROUTE_PATHS.ADMIN}/statistics/sales`),
        },
        {
          key: 'user-stats',
          label: '用户统计',
          onClick: () => navigate(`${ROUTE_PATHS.ADMIN}/statistics/users`),
        },
        {
          key: 'product-stats',
          label: '商品统计',
          onClick: () => navigate(`${ROUTE_PATHS.ADMIN}/statistics/products`),
        },
      ],
    },
    {
      key: 'settings',
      icon: <SettingOutlined />,
      label: '系统设置',
      onClick: () => navigate(`${ROUTE_PATHS.ADMIN}/settings`),
    },
  ];

  // 供应商侧边栏菜单
  const supplierMenuItems: MenuProps['items'] = [
    {
      key: 'supplier-dashboard',
      icon: <DashboardOutlined />,
      label: '仪表盘',
      onClick: () => navigate(ROUTE_PATHS.SUPPLIER_DASHBOARD),
    },
    {
      key: 'supplier-products',
      icon: <ShoppingOutlined />,
      label: '商品管理',
      onClick: () => navigate(ROUTE_PATHS.SUPPLIER_PRODUCTS),
    },
    {
      key: 'supplier-inventory',
      icon: <ContainerOutlined />,
      label: '库存管理',
      onClick: () => navigate(ROUTE_PATHS.SUPPLIER_INVENTORY),
    },
    {
      key: 'supplier-orders',
      icon: <OrderedListOutlined />,
      label: '订单处理',
      onClick: () => navigate(ROUTE_PATHS.SUPPLIER_ORDERS),
    },
    {
      key: 'supplier-statistics',
      icon: <BarChartOutlined />,
      label: '销售统计',
      onClick: () => navigate(`${ROUTE_PATHS.SUPPLIER}/statistics`),
    },
    {
      key: 'supplier-finance',
      icon: <BankOutlined />,
      label: '财务结算',
      onClick: () => navigate(`${ROUTE_PATHS.SUPPLIER}/finance`),
    },
  ];

  // 根据模式和用户角色确定显示哪个菜单
  const getMenuItems = () => {
    if (mode === 'admin') return adminMenuItems;
    if (mode === 'supplier') return supplierMenuItems;

    // 用户模式下，根据实际角色显示相应菜单
    if (user?.role === USER_ROLES.ADMIN) return adminMenuItems;
    if (user?.role === USER_ROLES.SUPPLIER) return supplierMenuItems;

    return userMenuItems;
  };

  // 获取当前选中的菜单项
  const getSelectedKeys = () => {
    const path = location.pathname;
    const items = getMenuItems();
    const findKey = (items: MenuProps['items']): string[] => {
      if (!items) return [];

      for (const item of items) {
        if (!item) continue;

        if ('children' in item && item.children) {
          const childResult = findKey(item.children);
          if (childResult.length > 0) {
            return [item.key as string, ...childResult];
          }
        }

        if ('onClick' in item && item.onClick) {
          // 这里简化处理，实际应该根据路由匹配
          // 暂时返回空，让Antd自动处理选中状态
        }
      }
      return [];
    };

    return findKey(items);
  };

  return (
    <Sider
      collapsible
      collapsed={sidebarCollapsed}
      onCollapse={() => {}}
      width={240}
      style={{
        background: 'transparent',
        borderRight: '1px solid #f0f0f0',
        overflow: 'auto',
        height: '100vh',
        position: 'sticky',
        top: 0,
        left: 0,
      }}
    >
      <Menu
        mode="inline"
        items={getMenuItems()}
        selectedKeys={getSelectedKeys()}
        style={{ height: '100%', borderRight: 0 }}
      />
    </Sider>
  );
};

export default Sidebar;