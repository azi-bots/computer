import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const SupplierDashboard: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={true} mode="supplier">
      <div style={{ padding: '24px' }}>
        <Title level={2}>供应商面板 - 仪表盘</Title>
        <p>供应商仪表盘正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default SupplierDashboard;