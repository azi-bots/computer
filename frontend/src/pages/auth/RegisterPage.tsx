import React, { useState } from 'react';
import { Form, Input, Button, Card, Typography, Space, message } from 'antd';
import {
  UserOutlined,
  LockOutlined,
  MailOutlined,
  PhoneOutlined,
} from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { authApi } from '@/api';
import { useAuthStore } from '@/store';
import { ROUTE_PATHS } from '@/config/constants';
import MainLayout from '@/components/layout/MainLayout';

const { Title, Text } = Typography;

const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const { login } = useAuthStore();
  const [loading, setLoading] = useState(false);

  const registerMutation = useMutation({
    mutationFn: authApi.register,
    onSuccess: (data) => {
      login(data);
      message.success('注册成功！');
      navigate(ROUTE_PATHS.HOME);
    },
    onError: (error) => {
      message.error('注册失败，请稍后重试');
    },
    onSettled: () => {
      setLoading(false);
    },
  });

  const onFinish = (values: any) => {
    setLoading(true);
    registerMutation.mutate(values);
  };

  const validatePassword = (_: any, value: string) => {
    if (!value) {
      return Promise.reject(new Error('请输入密码'));
    }
    if (value.length < 6) {
      return Promise.reject(new Error('密码至少6个字符'));
    }
    // 至少包含字母和数字
    if (!/(?=.*[a-zA-Z])(?=.*\d)/.test(value)) {
      return Promise.reject(new Error('密码必须包含字母和数字'));
    }
    return Promise.resolve();
  };

  const validateConfirmPassword = ({ getFieldValue }: any) => ({
    validator(_: any, value: string) {
      if (!value || getFieldValue('password') === value) {
        return Promise.resolve();
      }
      return Promise.reject(new Error('两次输入的密码不一致'));
    },
  });

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
          style={{ width: '100%', maxWidth: 480 }}
          bodyStyle={{ padding: '40px 32px' }}
          bordered={false}
          hoverable
        >
          <div style={{ textAlign: 'center', marginBottom: '32px' }}>
            <Title level={2}>用户注册</Title>
            <Text type="secondary">创建您的账户，开始购物之旅</Text>
          </div>

          <Form
            name="register"
            onFinish={onFinish}
            layout="vertical"
            size="large"
            scrollToFirstError
          >
            <Form.Item
              name="username"
              rules={[
                { required: true, message: '请输入用户名' },
                { min: 3, message: '用户名至少3个字符' },
                { max: 20, message: '用户名最多20个字符' },
                {
                  pattern: /^[a-zA-Z0-9_]+$/,
                  message: '用户名只能包含字母、数字和下划线',
                },
              ]}
            >
              <Input
                prefix={<UserOutlined />}
                placeholder="用户名"
                autoComplete="username"
              />
            </Form.Item>

            <Form.Item
              name="email"
              rules={[
                { required: true, message: '请输入邮箱地址' },
                {
                  type: 'email',
                  message: '请输入有效的邮箱地址',
                },
              ]}
            >
              <Input
                prefix={<MailOutlined />}
                placeholder="邮箱地址"
                autoComplete="email"
              />
            </Form.Item>

            <Form.Item
              name="phone"
              rules={[
                {
                  pattern: /^1[3-9]\d{9}$/,
                  message: '请输入有效的手机号码',
                },
              ]}
            >
              <Input
                prefix={<PhoneOutlined />}
                placeholder="手机号码（可选）"
                autoComplete="tel"
              />
            </Form.Item>

            <Form.Item
              name="password"
              rules={[
                { required: true, message: '请输入密码' },
                { validator: validatePassword },
              ]}
            >
              <Input.Password
                prefix={<LockOutlined />}
                placeholder="密码"
                autoComplete="new-password"
              />
            </Form.Item>

            <Form.Item
              name="confirmPassword"
              dependencies={['password']}
              rules={[
                { required: true, message: '请确认密码' },
                validateConfirmPassword,
              ]}
            >
              <Input.Password
                prefix={<LockOutlined />}
                placeholder="确认密码"
                autoComplete="new-password"
              />
            </Form.Item>

            <Form.Item>
              <div style={{ marginBottom: '16px' }}>
                <Text type="secondary" style={{ fontSize: '12px' }}>
                  注册即表示您同意我们的
                  <Link to="#" style={{ marginLeft: '4px' }}>
                    服务条款
                  </Link>
                  和
                  <Link to="#" style={{ marginLeft: '4px' }}>
                    隐私政策
                  </Link>
                </Text>
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
                注册
              </Button>
            </Form.Item>

            <div style={{ textAlign: 'center' }}>
              <Space>
                <Text>已有账户？</Text>
                <Link to={ROUTE_PATHS.LOGIN}>
                  <Button type="link" style={{ padding: 0 }}>
                    立即登录
                  </Button>
                </Link>
              </Space>
            </div>
          </Form>
        </Card>
      </div>
    </MainLayout>
  );
};

export default RegisterPage;