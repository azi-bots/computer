import React, { useState } from 'react';
import { Layout, Menu, Button, Avatar, Badge, Input, Dropdown, Space, Typography } from 'antd';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  ShoppingCartOutlined,
  BellOutlined,
  SearchOutlined,
  LogoutOutlined,
  DashboardOutlined,
  HomeOutlined,
} from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';
import { useUIStore, useAuthStore, useCartStore } from '@/store';
import { ROUTE_PATHS, USER_ROLES } from '@/config/constants';
import type { MenuProps } from 'antd';

const { Header: AntHeader } = Layout;
const { Title } = Typography;

const AppHeader: React.FC = () => {
  const navigate = useNavigate();
  const [searchValue, setSearchValue] = useState('');
  const { sidebarCollapsed, toggleSidebar, theme, toggleTheme } = useUIStore();
  const { user, logout, isAuthenticated } = useAuthStore();
  const { itemCount } = useCartStore();

  const handleSearch = (value: string) => {
    if (value.trim()) {
      navigate(`${ROUTE_PATHS.PRODUCTS}?keyword=${encodeURIComponent(value)}`);
    }
  };

  const handleLogout = () => {
    logout();
    navigate(ROUTE_PATHS.HOME);
  };

  const userMenuItems: MenuProps['items'] = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心',
      onClick: () => navigate(ROUTE_PATHS.PROFILE),
    },
    {
      key: 'orders',
      icon: <DashboardOutlined />,
      label: '我的订单',
      onClick: () => navigate(ROUTE_PATHS.ORDERS),
    },
    {
      type: 'divider',
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ];

  // 根据用户角色显示不同的导航菜单
  const getNavItems = () => {
    const baseItems = [
      {
        key: 'home',
        icon: <HomeOutlined />,
        label: '首页',
        onClick: () => navigate(ROUTE_PATHS.HOME),
      },
      {
        key: 'products',
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
    ];

    if (user?.role === USER_ROLES.ADMIN) {
      baseItems.push({
        key: 'admin',
        label: '管理后台',
        onClick: () => navigate(ROUTE_PATHS.ADMIN),
      });
    }

    if (user?.role === USER_ROLES.SUPPLIER) {
      baseItems.push({
        key: 'supplier',
        label: '供应商面板',
        onClick: () => navigate(ROUTE_PATHS.SUPPLIER),
      });
    }

    return baseItems;
  };

  return (
    <AntHeader
      style={{
        position: 'sticky',
        top: 0,
        zIndex: 100,
        width: '100%',
        display: 'flex',
        alignItems: 'center',
        padding: '0 24px',
        background: theme === 'dark' ? '#141414' : '#fff',
        borderBottom: `1px solid ${theme === 'dark' ? '#303030' : '#f0f0f0'}`,
      }}
    >
      {/* 左侧：Logo和菜单切换 */}
      <div style={{ display: 'flex', alignItems: 'center', marginRight: '24px' }}>
        <Button
          type="text"
          icon={sidebarCollapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          onClick={toggleSidebar}
          style={{ fontSize: '16px', width: 48, height: 48 }}
        />
        <Link to={ROUTE_PATHS.HOME} style={{ textDecoration: 'none', marginLeft: '16px' }}>
          <Title level={4} style={{ margin: 0, color: theme === 'dark' ? '#fff' : '#1890ff' }}>
            计算机电商平台
          </Title>
        </Link>
      </div>

      {/* 中间：导航菜单 */}
      <Menu
        mode="horizontal"
        items={getNavItems()}
        style={{
          flex: 1,
          minWidth: 0,
          border: 'none',
          background: 'transparent',
        }}
      />

      {/* 右侧：搜索和用户操作 */}
      <Space size="middle" style={{ marginLeft: 'auto' }}>
        {/* 搜索框 */}
        <Input
          placeholder="搜索商品..."
          prefix={<SearchOutlined />}
          value={searchValue}
          onChange={(e) => setSearchValue(e.target.value)}
          onPressEnter={(e) => handleSearch((e.target as HTMLInputElement).value)}
          style={{ width: 200 }}
        />

        {/* 主题切换 */}
        <Button
          type="text"
          icon={theme === 'dark' ? '🌙' : '☀️'}
          onClick={toggleTheme}
          style={{ fontSize: '18px' }}
        />

        {/* 购物车 */}
        <Badge count={itemCount} size="small">
          <Button
            type="text"
            icon={<ShoppingCartOutlined style={{ fontSize: '20px' }} />}
            onClick={() => navigate(ROUTE_PATHS.CART)}
            style={{ fontSize: '18px' }}
          />
        </Badge>

        {/* 通知 */}
        <Badge dot>
          <Button
            type="text"
            icon={<BellOutlined style={{ fontSize: '20px' }} />}
            onClick={() => {}}
            style={{ fontSize: '18px' }}
          />
        </Badge>

        {/* 用户信息 */}
        {isAuthenticated && user ? (
          <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
            <Space style={{ cursor: 'pointer', padding: '4px 8px', borderRadius: '4px' }}>
              <Avatar
                src={user.avatar}
                icon={!user.avatar && <UserOutlined />}
                style={{ backgroundColor: '#1890ff' }}
              />
              <span style={{ color: theme === 'dark' ? '#fff' : '#000' }}>
                {user.username}
              </span>
            </Space>
          </Dropdown>
        ) : (
          <Space>
            <Button type="text" onClick={() => navigate(ROUTE_PATHS.LOGIN)}>
              登录
            </Button>
            <Button type="primary" onClick={() => navigate(ROUTE_PATHS.REGISTER)}>
              注册
            </Button>
          </Space>
        )}
      </Space>
    </AntHeader>
  );
};

export default AppHeader;