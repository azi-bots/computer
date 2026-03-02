import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const ProfilePage: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={false}>
      <div style={{ padding: '24px' }}>
        <Title level={2}>个人中心</Title>
        <p>个人中心页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default ProfilePage;