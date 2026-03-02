import React, { useEffect, useState } from 'react';
import {
  Row,
  Col,
  Card,
  Carousel,
  Button,
  Typography,
  Space,
  Statistic,
  Tag,
  List,
  Avatar,
  Badge,
} from 'antd';
import {
  ShoppingOutlined,
  RocketOutlined,
  SafetyOutlined,
  StarOutlined,
  FireOutlined,
  ThunderboltOutlined,
  RightOutlined,
} from '@ant-design/icons';
import { Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { productApi } from '@/api';
import { QUERY_KEYS } from '@/config/queryClient';
import { ROUTE_PATHS } from '@/config/constants';
import MainLayout from '@/components/layout/MainLayout';
import './HomePage.css';

const { Title, Text, Paragraph } = Typography;

const HomePage: React.FC = () => {
  const [hotProducts, setHotProducts] = useState<any[]>([]);
  const [newProducts, setNewProducts] = useState<any[]>([]);

  // 获取热门商品
  const { data: hotProductsData } = useQuery({
    queryKey: [QUERY_KEYS.PRODUCTS, 'hot'],
    queryFn: () => productApi.getHotProducts(8),
  });

  // 获取新品
  const { data: newProductsData } = useQuery({
    queryKey: [QUERY_KEYS.PRODUCTS, 'new'],
    queryFn: () => productApi.getNewProducts(8),
  });

  useEffect(() => {
    if (hotProductsData) setHotProducts(hotProductsData);
    if (newProductsData) setNewProducts(newProductsData);
  }, [hotProductsData, newProductsData]);

  // 轮播图数据
  const carouselItems = [
    {
      id: 1,
      title: '高性能游戏笔记本',
      description: '最新RTX 40系列显卡，畅玩3A大作',
      image: 'https://picsum.photos/1200/400?random=1',
      link: ROUTE_PATHS.PRODUCTS,
      color: '#1890ff',
    },
    {
      id: 2,
      title: 'DIY电脑配件',
      description: '组装你的专属电脑，性价比之选',
      image: 'https://picsum.photos/1200/400?random=2',
      link: ROUTE_PATHS.PRODUCTS,
      color: '#52c41a',
    },
    {
      id: 3,
      title: '办公设备套装',
      description: '提升工作效率，专业办公解决方案',
      image: 'https://picsum.photos/1200/400?random=3',
      link: ROUTE_PATHS.PRODUCTS,
      color: '#722ed1',
    },
  ];

  // 特色服务
  const features = [
    {
      icon: <RocketOutlined style={{ fontSize: '32px', color: '#1890ff' }} />,
      title: '快速配送',
      description: '全国范围内24小时送达',
    },
    {
      icon: <SafetyOutlined style={{ fontSize: '32px', color: '#52c41a' }} />,
      title: '正品保证',
      description: '所有商品官方授权，假一赔十',
    },
    {
      icon: <StarOutlined style={{ fontSize: '32px', color: '#faad14' }} />,
      title: '专业服务',
      description: '7x24小时专业客服支持',
    },
    {
      icon: <ShoppingOutlined style={{ fontSize: '32px', color: '#722ed1' }} />,
      title: '无忧退货',
      description: '15天无理由退换货',
    },
  ];

  // 商品分类
  const categories = [
    { id: 1, name: '笔记本电脑', icon: '💻', count: 156 },
    { id: 2, name: '台式电脑', icon: '🖥️', count: 89 },
    { id: 3, name: '电脑配件', icon: '🔧', count: 342 },
    { id: 4, name: '外设设备', icon: '🎮', count: 213 },
    { id: 5, name: '网络设备', icon: '🌐', count: 67 },
    { id: 6, name: '办公设备', icon: '🖨️', count: 124 },
    { id: 7, name: '软件服务', icon: '📱', count: 45 },
    { id: 8, name: '维修服务', icon: '🔧', count: 32 },
  ];

  return (
    <MainLayout showHeader={true} showSidebar={false} showFooter={true}>
      <div className="home-page">
        {/* 轮播图 */}
        <section className="carousel-section">
          <Carousel autoplay effect="fade" style={{ borderRadius: '8px', overflow: 'hidden' }}>
            {carouselItems.map((item) => (
              <div key={item.id}>
                <div
                  style={{
                    background: `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(${item.image})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    height: '400px',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    color: 'white',
                    textAlign: 'center',
                    padding: '0 20px',
                  }}
                >
                  <div>
                    <Title level={2} style={{ color: 'white', marginBottom: '16px' }}>
                      {item.title}
                    </Title>
                    <Paragraph style={{ fontSize: '18px', color: 'white', marginBottom: '32px' }}>
                      {item.description}
                    </Paragraph>
                    <Link to={item.link}>
                      <Button type="primary" size="large" style={{ background: item.color }}>
                        立即购买
                      </Button>
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          </Carousel>
        </section>

        {/* 特色服务 */}
        <section className="features-section" style={{ margin: '48px 0' }}>
          <Row gutter={[32, 32]}>
            {features.map((feature, index) => (
              <Col xs={24} sm={12} md={6} key={index}>
                <Card
                  hoverable
                  style={{ textAlign: 'center', height: '100%' }}
                  bodyStyle={{ padding: '24px' }}
                >
                  <div style={{ marginBottom: '16px' }}>{feature.icon}</div>
                  <Title level={4} style={{ marginBottom: '8px' }}>
                    {feature.title}
                  </Title>
                  <Text type="secondary">{feature.description}</Text>
                </Card>
              </Col>
            ))}
          </Row>
        </section>

        {/* 商品分类 */}
        <section className="categories-section" style={{ margin: '48px 0' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
            <Title level={3}>商品分类</Title>
            <Link to={ROUTE_PATHS.CATEGORIES}>
              <Button type="link" icon={<RightOutlined />}>
                查看全部
              </Button>
            </Link>
          </div>
          <Row gutter={[16, 16]}>
            {categories.map((category) => (
              <Col xs={12} sm={8} md={6} lg={3} key={category.id}>
                <Link to={`${ROUTE_PATHS.PRODUCTS}?categoryId=${category.id}`}>
                  <Card
                    hoverable
                    style={{ textAlign: 'center', height: '100%' }}
                    bodyStyle={{ padding: '16px' }}
                  >
                    <div style={{ fontSize: '32px', marginBottom: '8px' }}>{category.icon}</div>
                    <Text strong style={{ display: 'block', marginBottom: '4px' }}>
                      {category.name}
                    </Text>
                    <Text type="secondary">{category.count}件商品</Text>
                  </Card>
                </Link>
              </Col>
            ))}
          </Row>
        </section>

        {/* 热门商品 */}
        <section className="hot-products-section" style={{ margin: '48px 0' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
            <Space>
              <FireOutlined style={{ color: '#ff4d4f', fontSize: '24px' }} />
              <Title level={3} style={{ margin: 0 }}>热销商品</Title>
            </Space>
            <Link to={`${ROUTE_PATHS.PRODUCTS}?sort=sales&order=desc`}>
              <Button type="link" icon={<RightOutlined />}>
                更多热销
              </Button>
            </Link>
          </div>
          <Row gutter={[24, 24]}>
            {hotProducts.map((product) => (
              <Col xs={24} sm={12} md={8} lg={6} key={product.id}>
                <Link to={`${ROUTE_PATHS.PRODUCTS}/${product.id}`}>
                  <Card
                    hoverable
                    cover={
                      <div style={{ height: '200px', overflow: 'hidden' }}>
                        <img
                          alt={product.name}
                          src={product.images?.[0] || 'https://picsum.photos/300/200'}
                          style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                        />
                      </div>
                    }
                  >
                    <Card.Meta
                      title={
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Text ellipsis style={{ maxWidth: '70%' }}>
                            {product.name}
                          </Text>
                          {product.status === 'OUT_OF_STOCK' && (
                            <Tag color="error">缺货</Tag>
                          )}
                        </div>
                      }
                      description={
                        <div>
                          <div style={{ marginBottom: '8px' }}>
                            <Text type="secondary" ellipsis>
                              {product.description || '暂无描述'}
                            </Text>
                          </div>
                          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <Statistic
                              value={product.price}
                              prefix="¥"
                              valueStyle={{ fontSize: '18px', color: '#ff4d4f' }}
                            />
                            <Badge count={`库存 ${product.stock}`} style={{ backgroundColor: '#52c41a' }} />
                          </div>
                        </div>
                      }
                    />
                  </Card>
                </Link>
              </Col>
            ))}
          </Row>
        </section>

        {/* 新品上市 */}
        <section className="new-products-section" style={{ margin: '48px 0' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
            <Space>
              <ThunderboltOutlined style={{ color: '#faad14', fontSize: '24px' }} />
              <Title level={3} style={{ margin: 0 }}>新品上市</Title>
            </Space>
            <Link to={`${ROUTE_PATHS.PRODUCTS}?sort=new&order=desc`}>
              <Button type="link" icon={<RightOutlined />}>
                更多新品
              </Button>
            </Link>
          </div>
          <Row gutter={[24, 24]}>
            {newProducts.map((product) => (
              <Col xs={24} sm={12} md={8} lg={6} key={product.id}>
                <Link to={`${ROUTE_PATHS.PRODUCTS}/${product.id}`}>
                  <Card
                    hoverable
                    cover={
                      <div style={{ height: '200px', overflow: 'hidden', position: 'relative' }}>
                        <img
                          alt={product.name}
                          src={product.images?.[0] || 'https://picsum.photos/300/200'}
                          style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                        />
                        <Tag
                          color="blue"
                          style={{ position: 'absolute', top: '8px', left: '8px' }}
                        >
                          新品
                        </Tag>
                      </div>
                    }
                  >
                    <Card.Meta
                      title={
                        <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                          <Text ellipsis style={{ maxWidth: '70%' }}>
                            {product.name}
                          </Text>
                          {product.status === 'OUT_OF_STOCK' && (
                            <Tag color="error">缺货</Tag>
                          )}
                        </div>
                      }
                      description={
                        <div>
                          <div style={{ marginBottom: '8px' }}>
                            <Text type="secondary" ellipsis>
                              {product.description || '暂无描述'}
                            </Text>
                          </div>
                          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <Statistic
                              value={product.price}
                              prefix="¥"
                              valueStyle={{ fontSize: '18px', color: '#ff4d4f' }}
                            />
                            <Button type="primary" size="small">
                              立即购买
                            </Button>
                          </div>
                        </div>
                      }
                    />
                  </Card>
                </Link>
              </Col>
            ))}
          </Row>
        </section>

        {/* 底部宣传 */}
        <section className="promotion-section" style={{ margin: '48px 0' }}>
          <Card
            style={{
              background: 'linear-gradient(135deg, #1890ff 0%, #36cfc9 100%)',
              color: 'white',
              borderRadius: '8px',
            }}
          >
            <Row align="middle" gutter={[48, 24]}>
              <Col xs={24} md={12}>
                <Title level={2} style={{ color: 'white' }}>
                  加入我们，享受会员特权
                </Title>
                <Paragraph style={{ color: 'white', fontSize: '16px', marginBottom: '32px' }}>
                  注册成为会员，享受专属折扣、优先发货、专属客服等多项特权
                </Paragraph>
                <Space>
                  <Link to={ROUTE_PATHS.REGISTER}>
                    <Button type="primary" size="large" style={{ background: 'white', color: '#1890ff' }}>
                      立即注册
                    </Button>
                  </Link>
                  <Link to={ROUTE_PATHS.LOGIN}>
                    <Button size="large" style={{ background: 'transparent', color: 'white', borderColor: 'white' }}>
                      会员登录
                    </Button>
                  </Link>
                </Space>
              </Col>
              <Col xs={24} md={12}>
                <div style={{ textAlign: 'center' }}>
                  <ShoppingOutlined style={{ fontSize: '120px', opacity: 0.8 }} />
                </div>
              </Col>
            </Row>
          </Card>
        </section>
      </div>
    </MainLayout>
  );
};

export default HomePage;