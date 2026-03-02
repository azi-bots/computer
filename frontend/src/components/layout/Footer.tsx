import React from 'react';
import { Layout, Row, Col, Typography, Space } from 'antd';
import {
  GithubOutlined,
  WechatOutlined,
  QqOutlined,
  AlipayCircleOutlined,
  PhoneOutlined,
  MailOutlined,
} from '@ant-design/icons';
import { useUIStore } from '@/store';

const { Footer: AntFooter } = Layout;
const { Text, Link: AntLink } = Typography;

const Footer: React.FC = () => {
  const theme = useUIStore((state) => state.theme);

  const footerLinks = [
    {
      title: '购物指南',
      links: [
        { label: '购物流程', href: '#' },
        { label: '会员介绍', href: '#' },
        { label: '常见问题', href: '#' },
        { label: '联系客服', href: '#' },
      ],
    },
    {
      title: '配送方式',
      links: [
        { label: '上门自提', href: '#' },
        { label: '快递配送', href: '#' },
        { label: '配送服务查询', href: '#' },
        { label: '配送费收取标准', href: '#' },
      ],
    },
    {
      title: '支付方式',
      links: [
        { label: '货到付款', href: '#' },
        { label: '在线支付', href: '#' },
        { label: '分期付款', href: '#' },
        { label: '邮局汇款', href: '#' },
      ],
    },
    {
      title: '售后服务',
      links: [
        { label: '售后政策', href: '#' },
        { label: '价格保护', href: '#' },
        { label: '退款说明', href: '#' },
        { label: '返修/退换货', href: '#' },
      ],
    },
  ];

  return (
    <AntFooter
      style={{
        background: theme === 'dark' ? '#141414' : '#f0f2f5',
        padding: '48px 24px',
        marginTop: '24px',
        borderTop: `1px solid ${theme === 'dark' ? '#303030' : '#d9d9d9'}`,
      }}
    >
      <div style={{ maxWidth: 1200, margin: '0 auto' }}>
        {/* 上部分：链接区域 */}
        <Row gutter={[48, 32]}>
          {footerLinks.map((column) => (
            <Col xs={24} sm={12} md={6} key={column.title}>
              <div style={{ marginBottom: '16px' }}>
                <Text strong style={{ fontSize: '16px', color: theme === 'dark' ? '#fff' : '#000' }}>
                  {column.title}
                </Text>
              </div>
              <Space direction="vertical" size="small">
                {column.links.map((link) => (
                  <AntLink
                    key={link.label}
                    href={link.href}
                    style={{ color: theme === 'dark' ? '#bfbfbf' : '#595959' }}
                  >
                    {link.label}
                  </AntLink>
                ))}
              </Space>
            </Col>
          ))}
        </Row>

        {/* 中部分：联系方式和社交媒体 */}
        <Row
          gutter={[32, 16]}
          style={{ marginTop: '48px', paddingTop: '24px', borderTop: '1px solid #d9d9d9' }}
        >
          <Col xs={24} md={12}>
            <div style={{ marginBottom: '16px' }}>
              <Text strong style={{ fontSize: '16px', color: theme === 'dark' ? '#fff' : '#000' }}>
                联系我们
              </Text>
            </div>
            <Space direction="vertical" size="middle">
              <Space>
                <PhoneOutlined style={{ color: '#1890ff' }} />
                <Text style={{ color: theme === 'dark' ? '#bfbfbf' : '#595959' }}>
                  客服热线：400-123-4567
                </Text>
              </Space>
              <Space>
                <MailOutlined style={{ color: '#1890ff' }} />
                <Text style={{ color: theme === 'dark' ? '#bfbfbf' : '#595959' }}>
                  邮箱：service@computer-ecommerce.com
                </Text>
              </Space>
              <Space>
                <Text style={{ color: theme === 'dark' ? '#bfbfbf' : '#595959' }}>
                  工作时间：周一至周日 9:00-21:00
                </Text>
              </Space>
            </Space>
          </Col>
          <Col xs={24} md={12}>
            <div style={{ marginBottom: '16px' }}>
              <Text strong style={{ fontSize: '16px', color: theme === 'dark' ? '#fff' : '#000' }}>
                关注我们
              </Text>
            </div>
            <Space size="large">
              <AntLink href="#" style={{ fontSize: '24px', color: '#333' }}>
                <WechatOutlined />
              </AntLink>
              <AntLink href="#" style={{ fontSize: '24px', color: '#333' }}>
                <QqOutlined />
              </AntLink>
              <AntLink href="#" style={{ fontSize: '24px', color: '#333' }}>
                <AlipayCircleOutlined />
              </AntLink>
              <AntLink href="#" style={{ fontSize: '24px', color: '#333' }}>
                <GithubOutlined />
              </AntLink>
            </Space>
          </Col>
        </Row>

        {/* 下部分：版权信息 */}
        <Row style={{ marginTop: '48px', paddingTop: '24px', borderTop: '1px solid #d9d9d9' }}>
          <Col span={24}>
            <div style={{ textAlign: 'center' }}>
              <Text style={{ color: theme === 'dark' ? '#8c8c8c' : '#8c8c8c' }}>
                © 2026 计算机电商平台 版权所有
              </Text>
              <div style={{ marginTop: '8px' }}>
                <Space size="middle">
                  <AntLink href="#" style={{ color: theme === 'dark' ? '#8c8c8c' : '#8c8c8c' }}>
                    隐私政策
                  </AntLink>
                  <AntLink href="#" style={{ color: theme === 'dark' ? '#8c8c8c' : '#8c8c8c' }}>
                    用户协议
                  </AntLink>
                  <AntLink href="#" style={{ color: theme === 'dark' ? '#8c8c8c' : '#8c8c8c' }}>
                    法律声明
                  </AntLink>
                  <AntLink href="#" style={{ color: theme === 'dark' ? '#8c8c8c' : '#8c8c8c' }}>
                    帮助中心
                  </AntLink>
                </Space>
              </div>
              <div style={{ marginTop: '8px' }}>
                <Text type="secondary" style={{ fontSize: '12px' }}>
                  京ICP备12345678号-1 | 京公网安备11010802012345号
                </Text>
              </div>
            </div>
          </Col>
        </Row>
      </div>
    </AntFooter>
  );
};

export default Footer;