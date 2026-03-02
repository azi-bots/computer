import React, { useState } from 'react';
import { Form, Input, Button, Card, Typography, Space, message } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { authApi } from '@/api';
import { useAuthStore } from '@/store';
import { ROUTE_PATHS } from '@/config/constants';
import MainLayout from '@/components/layout/MainLayout';

const { Title, Text } = Typography;

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const { login } = useAuthStore();
  const [loading, setLoading] = useState(false);

  const loginMutation = useMutation({
    mutationFn: authApi.login,
    onSuccess: (data) => {
      login(data);
      message.success('登录成功！');
      navigate(ROUTE_PATHS.HOME);
    },
    onError: (error) => {
      message.error('登录失败，请检查用户名和密码');
    },
    onSettled: () => {
      setLoading(false);
    },
  });

  const onFinish = (values: { username: string; password: string }) => {
    setLoading(true);
    loginMutation.mutate(values);
  };

  return (
    <MainLayout showHeader={true} showSidebar={false} showFooter={false}>
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: 'calc(100vh - 64px)',
          padding: '24px',
        }}
      >
        <Card
          style={{ width: '100%', maxWidth: 400 }}
          bodyStyle={{ padding: '40px 32px' }}
          bordered={false}
          hoverable
        >
          <div style={{ textAlign: 'center', marginBottom: '32px' }}>
            <Title level={2}>用户登录</Title>
            <Text type="secondary">欢迎回来，请登录您的账户</Text>
          </div>

          <Form
            name="login"
            initialValues={{ remember: true }}
            onFinish={onFinish}
            layout="vertical"
            size="large"
          >
            <Form.Item
              name="username"
              rules={[
                { required: true, message: '请输入用户名' },
                { min: 3, message: '用户名至少3个字符' },
                { max: 20, message: '用户名最多20个字符' },
              ]}
            >
              <Input
                prefix={<UserOutlined />}
                placeholder="用户名"
                autoComplete="username"
              />
            </Form.Item>

            <Form.Item
              name="password"
              rules={[
                { required: true, message: '请输入密码' },
                { min: 6, message: '密码至少6个字符' },
              ]}
            >
              <Input.Password
                prefix={<LockOutlined />}
                placeholder="密码"
                autoComplete="current-password"
              />
            </Form.Item>

            <Form.Item>
              <div style={{ textAlign: 'right', marginBottom: '16px' }}>
                <Link to="#" style={{ fontSize: '14px' }}>
                  忘记密码？
                </Link>
              </div>
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                loading={loading}
                block
                size="large"
              >
                登录
              </Button>
            </Form.Item>

            <div style={{ textAlign: 'center' }}>
              <Space>
                <Text>还没有账户？</Text>
                <Link to={ROUTE_PATHS.REGISTER}>
                  <Button type="link" style={{ padding: 0 }}>
                    立即注册
                  </Button>
                </Link>
              </Space>
            </div>
          </Form>

          <div style={{ marginTop: '32px', textAlign: 'center' }}>
            <Text type="secondary" style={{ fontSize: '12px' }}>
              登录即表示您同意我们的服务条款和隐私政策
            </Text>
          </div>
        </Card>
      </div>
    </MainLayout>
  );
};

export default LoginPage;