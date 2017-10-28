import Vue from 'vue';
import VueRouter from 'vue-router';
import Main from '@/views/Main.vue';
const _import = require('./_import_' + process.env.NODE_ENV);
console.log(process.env.NODE_ENV);
Vue.use(VueRouter);

// 不作为Main组件的子页面展示的页面单独写，如下
export const loginRouter = {
    path: '/login',
    name: 'login',
    meta: {
        title: 'Login - 登录'
    },
    component: _import('login')
};

export const page404 = {
    path: '/*',
    name: 'error_404',
    meta: {
        title: '404-页面不存在'
    },
    component: _import('error_page/404')
};

export const page401 = {
    path: '/401',
    meta: {
        title: '401-权限不足'
    },
    name: 'error_401',
    component: _import('error_page/401')
};

export const page500 = {
    path: '/500',
    meta: {
        title: '500-服务端错误'
    },
    name: 'error_500',
    component: _import('error_page/500')
};

export const locking = {
    path: '/locking',
    name: 'locking',
    component: _import('main_components/locking-page')
};

// 作为Main组件的子页面展示但是不在左侧菜单显示的路由写在otherRouter里
export const otherRouter = {
    path: '/',
    name: 'otherRouter',
    component: Main,
    children: [
        {
            path: 'home',
            title: {
                i18n: 'home'
            },
            name: 'home',
            component: _import('home/home')
        },
        {
            path: 'ownspace',
            title: '个人中心',
            name: 'ownspace',
            component: _import('own-space/own-space')
        },
        {
            path: 'message',
            title: '消息中心',
            name: 'message',
            component: _import('message/message')
        }
    ]
};

// 作为Main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
export const appRouter = [
    {
        path: '/access',
        icon: 'key',
        name: 'access',
        title: '权限管理',
        component: Main,
        children: [
            {
                path: 'index',
                title: '权限管理',
                name: 'access',
                component: _import('access/access')
            }
        ]
    }
];

// 所有上面定义的路由都要写在下面的routers里
export const routers = [
    loginRouter,
    otherRouter,
    locking,
    ...appRouter,
    page500,
    page401,
    page404
];

// 路由配置
const RouterConfig = {
    // mode: 'history',
    scrollBehavior: () => ({
        y: 0
    }),
    routes: routers
};

export const router = new VueRouter(RouterConfig);
