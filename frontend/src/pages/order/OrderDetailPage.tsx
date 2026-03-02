import React from 'react';
import { Typography } from 'antd';
import { useParams } from 'react-router-dom';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const OrderDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();

  return (
    <MainLayout showHeader={true} showSidebar={false}>
      <div style={{ padding: '24px' }}>
        <Title level={2}>订单详情</Title>
        <p>订单ID: {id}</p>
        <p>订单详情页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default OrderDetailPage;