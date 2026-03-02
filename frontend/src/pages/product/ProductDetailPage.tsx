import React from 'react';
import { Typography } from 'antd';
import { useParams } from 'react-router-dom';
import MainLayout from '@/components/layout/MainLayout';

const { Title } = Typography;

const ProductDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();

  return (
    <MainLayout showHeader={true} showSidebar={false}>
      <div style={{ padding: '24px' }}>
        <Title level={2}>商品详情</Title>
        <p>商品ID: {id}</p>
        <p>商品详情页面正在开发中...</p>
      </div>
    </MainLayout>
  );
};

export default ProductDetailPage;