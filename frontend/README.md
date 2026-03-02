# 计算机电商平台 - React前端

这是计算机电商平台的React前端应用，基于Vite + React + TypeScript构建。

## 功能特性

- 🚀 基于Vite的极速开发体验
- ⚛️ React 18 + TypeScript
- 🎨 Ant Design 5 UI组件库
- 📱 响应式设计，支持移动端
- 🔐 JWT认证和路由守卫
- 🛒 完整的购物车功能
- 📊 管理后台和供应商面板
- 🔄 React Query数据管理
- 🗃️ Zustand状态管理
- 📝 Formik表单处理

## 项目结构

```
frontend/
├── src/
│   ├── api/              # API服务层
│   ├── assets/           # 静态资源
│   ├── components/       # 公共组件
│   │   ├── layout/      # 布局组件
│   │   ├── ui/          # UI基础组件
│   │   └── shared/      # 共享组件
│   ├── config/          # 配置文件
│   ├── contexts/        # React Context
│   ├── hooks/           # 自定义Hooks
│   ├── pages/           # 页面组件
│   │   ├── auth/       # 认证页面
│   │   ├── home/       # 首页
│   │   ├── product/    # 商品相关
│   │   ├── cart/       # 购物车
│   │   ├── order/      # 订单相关
│   │   ├── user/       # 用户中心
│   │   ├── admin/      # 管理后台
│   │   └── supplier/   # 供应商面板
│   ├── store/          # Zustand状态存储
│   ├── types/          # TypeScript类型定义
│   ├── utils/          # 工具函数
│   ├── App.tsx         # 根组件
│   └── main.tsx        # 入口文件
```

## 技术栈

- **框架**: React 18, TypeScript
- **构建工具**: Vite
- **路由**: React Router v6
- **UI组件库**: Ant Design v5
- **状态管理**: Zustand
- **数据获取**: React Query + Axios
- **表单处理**: Formik + Yup
- **样式**: Styled Components + CSS Modules
- **工具库**: Day.js, React Hook Form

## 环境要求

- Node.js >= 18.0.0
- npm >= 8.0.0

## 快速开始

### 1. 安装依赖

```bash
npm install
```

### 2. 配置环境变量

复制环境变量示例文件：

```bash
cp .env.example .env.development
```

根据你的后端API地址修改配置：

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_PORT=5173
VITE_APP_NAME=Computer E-commerce
```

### 3. 启动开发服务器

```bash
npm run dev
```

应用将在 http://localhost:5173 启动。

### 4. 构建生产版本

```bash
npm run build
```

构建结果将在 `dist/` 目录中。

### 5. 预览生产构建

```bash
npm run preview
```

## 开发指南

### 添加新页面

1. 在 `src/pages/` 下创建页面组件
2. 在 `src/config/routes.ts` 中添加路由配置
3. 在 `src/App.tsx` 中添加路由定义

### 添加API接口

1. 在 `src/api/` 下创建对应的API文件
2. 使用 `src/config/axios.ts` 中的 `http` 工具
3. 在 `src/types/index.ts` 中添加类型定义

### 状态管理

- 用户认证状态: `src/store/auth.store.ts`
- 购物车状态: `src/store/cart.store.ts`
- UI状态: `src/store/ui.store.ts`

### 代码规范

- 使用ESLint进行代码检查
- 使用Prettier进行代码格式化
- 组件使用函数组件和React Hooks
- 使用TypeScript严格模式

## API集成

前端与后端的Spring Boot应用通过REST API通信。确保后端服务正在运行：

```bash
# 在 computer-ecommerce 目录中
mvn spring-boot:run
```

后端默认运行在 http://localhost:8080

## 用户角色

系统支持三种用户角色：

1. **普通用户 (USER)**: 浏览商品、下单、管理个人资料
2. **管理员 (ADMIN)**: 管理后台、商品、订单、用户
3. **供应商 (SUPPLIER)**: 管理库存、处理订单、上架商品

## 开发计划

### 第一阶段：基础搭建 ✅
- [x] 创建Vite + React + TypeScript项目
- [x] 配置开发环境和核心依赖
- [x] 实现基础布局组件
- [x] 配置Axios和API服务层

### 第二阶段：认证和用户模块
- [ ] 完善登录/注册页面
- [ ] 实现JWT认证流程
- [ ] 创建路由守卫
- [ ] 实现个人中心和地址管理

### 第三阶段：商品和购物车模块
- [ ] 实现商品列表页面
- [ ] 实现商品详情页面
- [ ] 完善购物车功能
- [ ] 实现分类浏览

### 第四阶段：订单和支付模块
- [ ] 实现下单流程
- [ ] 实现订单管理
- [ ] 集成支付功能
- [ ] 实现订单状态跟踪

### 第五阶段：管理后台
- [ ] 实现管理后台布局
- [ ] 实现商品管理（CRUD）
- [ ] 实现订单管理
- [ ] 实现用户管理
- [ ] 实现数据统计仪表盘

### 第六阶段：供应商面板
- [ ] 实现供应商面板布局
- [ ] 实现库存管理
- [ ] 实现订单处理
- [ ] 实现销售统计

### 第七阶段：优化和测试
- [ ] 性能优化
- [ ] 响应式适配
- [ ] 错误边界处理
- [ ] 单元测试和集成测试

## 常见问题

### 1. 端口被占用

如果默认端口5173被占用，Vite会自动选择其他端口。检查控制台输出确认实际端口。

### 2. 代理配置

开发环境下，API请求通过Vite代理到后端。配置见 `vite.config.ts`:

```typescript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
  },
}
```

### 3. 跨域问题

如果遇到跨域问题，确保后端已配置CORS。后端 `CorsConfig` 已配置允许所有来源。

### 4. 类型错误

运行 `npm run build` 检查TypeScript类型错误。

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目仅供学习使用。