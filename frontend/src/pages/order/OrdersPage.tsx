import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const OrdersPage: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={false}>
      <div style={{ padding: '24px' }}>
        <Title level={2}>我的订单</Title>
        <p>订单列表页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default OrdersPage;