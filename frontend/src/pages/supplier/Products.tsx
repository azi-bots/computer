import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const SupplierProducts: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={true} mode="supplier">
      <div style={{ padding: '24px' }}>
        <Title level={2}>供应商面板 - 商品管理</Title>
        <p>商品管理页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default SupplierProducts;