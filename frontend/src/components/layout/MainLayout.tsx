import React, { ReactNode } from 'react';
import { Layout, ConfigProvider, theme } from 'antd';
import { useUIStore } from '@/store';
import Header from './Header';
import Sidebar from './Sidebar';
import Footer from './Footer';

const { Content } = Layout;

interface MainLayoutProps {
  children: ReactNode;
  showHeader?: boolean;
  showSidebar?: boolean;
  showFooter?: boolean;
}

const MainLayout: React.FC<MainLayoutProps> = ({
  children,
  showHeader = true,
  showSidebar = false,
  showFooter = true,
}) => {
  const themeMode = useUIStore((state) => state.theme);

  return (
    <ConfigProvider
      theme={{
        algorithm: themeMode === 'dark' ? theme.darkAlgorithm : theme.defaultAlgorithm,
        token: {
          colorPrimary: '#1890ff',
          borderRadius: 6,
        },
      }}
    >
      <Layout style={{ minHeight: '100vh' }}>
        {showHeader && <Header />}
        <Layout>
          {showSidebar && <Sidebar />}
          <Layout style={{ padding: '24px' }}>
            <Content
              style={{
                padding: 24,
                margin: 0,
                minHeight: 280,
                background: themeMode === 'dark' ? '#141414' : '#fff',
                borderRadius: 8,
              }}
            >
              {children}
            </Content>
            {showFooter && <Footer />}
          </Layout>
        </Layout>
      </Layout>
    </ConfigProvider>
  );
};

export default MainLayout;