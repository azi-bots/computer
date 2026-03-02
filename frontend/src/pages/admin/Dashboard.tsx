import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const AdminDashboard: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={true} mode="admin">
      <div style={{ padding: '24px' }}>
        <Title level={2}>管理后台 - 仪表盘</Title>
        <p>管理后台仪表盘正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default AdminDashboard;