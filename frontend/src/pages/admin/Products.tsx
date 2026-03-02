import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const AdminProducts: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={true} mode="admin">
      <div style={{ padding: '24px' }}>
        <Title level={2}>管理后台 - 商品管理</Title>
        <p>商品管理页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default AdminProducts;