import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const SupplierOrders: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={true} mode="supplier">
      <div style={{ padding: '24px' }}>
        <Title level={2}>供应商面板 - 订单处理</Title>
        <p>订单处理页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default SupplierOrders;