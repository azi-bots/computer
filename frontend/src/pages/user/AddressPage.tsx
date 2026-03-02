import React from 'react';
import { Typography } from 'antd';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const AddressPage: React.FC = () => {
  return (
    <MainLayout showHeader={true} showSidebar={false}>
      <div style={{ padding: '24px' }}>
        <Title level={2}>地址管理</Title>
        <p>地址管理页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default AddressPage;