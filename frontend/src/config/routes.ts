import { ROUTE_PATHS, USER_ROLES } from './constants';
import type { RoutePermission } from '@/types';

// 路由权限配置
export const ROUTE_PERMISSIONS: RoutePermission[] = [
  // 公共路由（无需认证）
  {
    path: ROUTE_PATHS.HOME,
    roles: [],
    title: '首页',
  },
  {
    path: ROUTE_PATHS.LOGIN,
    roles: [],
    title: '登录',
  },
  {
    path: ROUTE_PATHS.REGISTER,
    roles: [],
    title: '注册',
  },
  {
    path: ROUTE_PATHS.PRODUCTS,
    roles: [],
    title: '商品列表',
  },
  {
    path: ROUTE_PATHS.PRODUCT_DETAIL,
    roles: [],
    title: '商品详情',
  },
  {
    path: ROUTE_PATHS.CATEGORIES,
    roles: [],
    title: '分类浏览',
  },

  // 用户路由（需要用户角色）
  {
    path: ROUTE_PATHS.CART,
    roles: [USER_ROLES.USER],
    title: '购物车',
  },
  {
    path: ROUTE_PATHS.CHECKOUT,
    roles: [USER_ROLES.USER],
    title: '结算',
  },
  {
    path: ROUTE_PATHS.ORDERS,
    roles: [USER_ROLES.USER],
    title: '我的订单',
  },
  {
    path: ROUTE_PATHS.ORDER_DETAIL,
    roles: [USER_ROLES.USER],
    title: '订单详情',
  },
  {
    path: ROUTE_PATHS.PROFILE,
    roles: [USER_ROLES.USER],
    title: '个人中心',
  },
  {
    path: ROUTE_PATHS.ADDRESSES,
    roles: [USER_ROLES.USER],
    title: '地址管理',
  },

  // 管理路由（需要管理员角色）
  {
    path: ROUTE_PATHS.ADMIN,
    roles: [USER_ROLES.ADMIN],
    title: '管理后台',
  },
  {
    path: ROUTE_PATHS.ADMIN_DASHBOARD,
    roles: [USER_ROLES.ADMIN],
    title: '数据仪表盘',
  },
  {
    path: ROUTE_PATHS.ADMIN_PRODUCTS,
    roles: [USER_ROLES.ADMIN],
    title: '商品管理',
  },
  {
    path: ROUTE_PATHS.ADMIN_ORDERS,
    roles: [USER_ROLES.ADMIN],
    title: '订单管理',
  },
  {
    path: ROUTE_PATHS.ADMIN_USERS,
    roles: [USER_ROLES.ADMIN],
    title: '用户管理',
  },
  {
    path: ROUTE_PATHS.ADMIN_CATEGORIES,
    roles: [USER_ROLES.ADMIN],
    title: '分类管理',
  },

  // 供应商路由（需要供应商角色）
  {
    path: ROUTE_PATHS.SUPPLIER,
    roles: [USER_ROLES.SUPPLIER],
    title: '供应商面板',
  },
  {
    path: ROUTE_PATHS.SUPPLIER_DASHBOARD,
    roles: [USER_ROLES.SUPPLIER],
    title: '供应商仪表盘',
  },
  {
    path: ROUTE_PATHS.SUPPLIER_INVENTORY,
    roles: [USER_ROLES.SUPPLIER],
    title: '库存管理',
  },
  {
    path: ROUTE_PATHS.SUPPLIER_ORDERS,
    roles: [USER_ROLES.SUPPLIER],
    title: '订单处理',
  },
  {
    path: ROUTE_PATHS.SUPPLIER_PRODUCTS,
    roles: [USER_ROLES.SUPPLIER],
    title: '商品管理',
  },
];

// 获取路由权限
export function getRoutePermission(path: string): RoutePermission | undefined {
  return ROUTE_PERMISSIONS.find((route) => {
    // 处理动态参数路由匹配
    if (route.path.includes(':')) {
      const routePattern = route.path.replace(/:\w+/g, '[^/]+');
      const regex = new RegExp(`^${routePattern}$`);
      return regex.test(path);
    }
    return route.path === path;
  });
}

// 检查用户是否有权限访问路由
export function hasRoutePermission(
  path: string,
  userRole?: string
): { hasPermission: boolean; title: string } {
  const routePermission = getRoutePermission(path);

  if (!routePermission) {
    // 未配置的路由默认允许访问
    return { hasPermission: true, title: '' };
  }

  if (routePermission.roles.length === 0) {
    // 公共路由
    return { hasPermission: true, title: routePermission.title };
  }

  if (!userRole) {
    // 需要认证但用户未登录
    return { hasPermission: false, title: routePermission.title };
  }

  const hasPermission = routePermission.roles.includes(userRole as any);
  return { hasPermission, title: routePermission.title };
}