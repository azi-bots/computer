import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const SupplierInventory: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={true} mode="supplier">
      <div style={{ padding: '24px' }}>
        <Title level={2}>供应商面板 - 库存管理</Title>
        <p>库存管理页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default SupplierInventory;