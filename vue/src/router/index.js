import Vue from 'vue'
import VueRouter from 'vue-router'
import Front from '../views/Front.vue'
import Home from '../views/front/Home.vue'
import Notice from '../views/front/Notice.vue'
import AndroidVersion from '../views/front/AndroidVersion.vue'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    redirect: '/front/home'
  },
  {
    path: '/front',
    component: Front,
    children: [
      {
        path: 'home',
        name: 'FrontHome',
        component: Home
      },
      {
        path: 'notice',
        name: 'FrontNotice',
        component: Notice
      },
      {
        path: 'android/all',
        name: 'FrontAndroidAll',
        component: () => import('@/views/front/FrontAllVersion.vue')
      },
      {
        path: 'android/10',
        name: 'FrontAndroid10',
        component: () => import('@/views/front/FrontCustomerPackage.vue'),
        props: { version: 'a10' }
      },
      {
        path: 'android/13',
        name: 'FrontAndroid13',
        component: () => import('@/views/front/FrontCustomerPackage.vue'),
        props: { version: 'a13' }
      },
      {
        path: 'android/14',
        name: 'FrontAndroid14',
        component: () => import('@/views/front/FrontCustomerPackage.vue'),
        props: { version: 'a14' }
      },
      {
        path: 'android/15',
        name: 'FrontAndroid15',
        component: () => import('@/views/front/FrontCustomerPackage.vue'),
        props: { version: 'a15' }
      },
      {
        path: 'android/:version',
        name: 'AndroidVersion',
        component: AndroidVersion,
        props: true
      },
      {
        path: 'test',
        name: 'FrontTest',
        component: () => import('@/views/front/Test.vue')
      }
    ]
  },
  {
    path: '/manager',
    name: 'Manager',
    component: () => import('../views/Manager'),
    meta: { name: '管理' },
    redirect: '/manager/home',
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告' }, component: () => import('../views/manager/Notice') },
      { 
        path: 'customer/:version/:category', 
        name: 'CustomerPackage', 
        meta: { name: '客户镜像包' }, 
        component: () => import('../views/customer/CustomerPackage.vue'),
        props: true
      },
      { 
        path: 'customer/:version', 
        name: 'CustomerPackageVersion', 
        meta: { name: '客户镜像包' }, 
        component: () => import('../views/customer/CustomerPackage.vue'),
        props: route => ({ 
          version: route.params.version, 
          category: 'vmos' // 默认显示VMOS
        })
      },
      { 
        path: 'customer-package/:version/:category', 
        name: 'CustomerPackageNew', 
        meta: { name: '客户镜像包' }, 
        component: () => import('../views/customer/CustomerPackage.vue'),
        props: true
      },
      { 
        path: 'customer-package/:version', 
        name: 'CustomerPackageVersionNew', 
        meta: { name: '客户镜像包' }, 
        component: () => import('../views/customer/CustomerPackage.vue'),
        props: route => ({ 
          version: route.params.version, 
          category: 'vmos' // 默认显示VMOS
        })
      },
      { path: 'lucky-wheel', name: 'LuckyWheel', meta: { name: '幸运大转盘' }, component: () => import('../views/LuckyWheel') },
      { path: 'a10', name: 'A10', meta: { name: 'Android 10' }, component: () => import('../views/manager/A10') },
      { path: 'a12', name: 'A12', meta: { name: 'Android 12' }, component: () => import('../views/manager/A12') },
      { path: 'a13', name: 'A13', meta: { name: 'Android 13' }, component: () => import('../views/manager/A13') },
      { path: 'a14', name: 'A14', meta: { name: 'Android 14' }, component: () => import('../views/manager/A14') },
      { path: 'a15', name: 'A15', meta: { name: 'Android 15' }, component: () => import('../views/manager/A15') }
    ]
  },
  { path: '/admin', redirect: '/manager/admin' },
  { path: '/notice', redirect: '/manager/notice' },
  // 添加customer-package路由重定向到manager下
  { path: '/customer-package/:version/:category', redirect: to => `/manager/customer-package/${to.params.version}/${to.params.category}` },
  { path: '/customer-package/:version', redirect: to => `/manager/customer-package/${to.params.version}` },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
  
  // 前台页面不需要登录
  if (to.path.startsWith('/front')) {
    next();
    return;
  }
  
  // 后台页面需要登录
  if (to.path.startsWith('/manager')) {
    if (user.role) {
      next();
    } else {
      next('/login');
    }
  } else if (to.path === '/login' || to.path === '/register') {
    // 如果已经登录，访问登录页则跳转到后台首页
    if (user.role) {
      next('/manager/home');
    } else {
      next();
    }
  } else {
    next();
  }
})

export default router
