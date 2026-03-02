import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const CartPage: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={false}>
      <div style={{ padding: '24px' }}>
        <Title level={2}>购物车</Title>
        <p>购物车页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default CartPage;