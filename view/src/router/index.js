import Vue from 'vue';
import VueRouter from 'vue-router';
import Main from '@/views/Main.vue';

Vue.use(VueRouter);

// 不作为Main组件的子页面展示的页面单独写，如下
export const loginRouter = {
    path: '/login',
    name: 'login',
    meta: {
        title: 'Login - 登录'
    },
    component: resolve => {
        require(['../views/login.vue'], resolve);
    }
};

export const page404 = {
    path: '/*',
    name: 'error_404',
    meta: {
        title: '404-页面不存在'
    },
    component: resolve => {
        require(['../views/error_page/404.vue'], resolve);
    }
};

export const page401 = {
    path: '/401',
    meta: {
        title: '401-权限不足'
    },
    name: 'error_401',
    component: resolve => {
        require(['../views/error_page/401.vue'], resolve);
    }
};

export const page500 = {
    path: '/500',
    meta: {
        title: '500-服务端错误'
    },
    name: 'error_500',
    component: resolve => {
        require(['../views/error_page/500.vue'], resolve);
    }
};

export const locking = {
    path: '/locking',
    name: 'locking',
    component: resolve => {
        require(['../views/components/locking-page.vue'], resolve);
    }
};

// 作为Main组件的子页面展示但是不在左侧菜单显示的路由写在otherRouter里
export const otherRouter = {
    path: '/',
    name: 'otherRouter',
    redirect: '/home',
    component: Main,
    children: [
        {
            path: '/home',
            title: {
                i18n: 'home'
            },
            name: 'home',
            component: resolve => {
                require(['../views/home/home.vue'], resolve);
            }
        },
        {
            path: '/ownspace',
            title: '个人中心',
            name: 'ownspace',
            component: resolve => {
                require(['../views/own-space/own-space.vue'], resolve);
            }
        },
        {
            path: '/message',
            title: '消息中心',
            name: 'message',
            component: resolve => {
                require(['../views/message/message.vue'], resolve);
            }
        }
    ]
};

// 作为Main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
export const appRouter = [
    {
        path: '/access',
        icon: 'key',
        name: 'AUTH',
        access: 'AUTH',
        title: '权限设置',
        component: Main,
        children: [
            {
                path: 'user',
                title: '用户管理',
                name: 'AUTH_USER',
                access: 'AUTH_USER',
                component: resolve => {
                    require(['../views/auth/auth-user.vue'], resolve);
                }
            },

            {
                path: 'role',
                title: '角色管理',
                name: 'AUTH_ROLE',
                access: 'AUTH_ROLE',
                component: resolve => {
                    require(['../views/auth/auth-role.vue'], resolve);
                }
            }
        ]
    },

    {
        path: '/access',
        icon: 'key',
        name: 'AUTH_NONE',
        access: 'AUTH_NONE',
        title: '英索纳管理',
        component: Main,
        children: [
            {
                path: 'info',
                title: '信息管理',
                name: 'INFO',
                component: resolve => {
                    require(['../views/insona/insona-info.vue'], resolve);
                }
            },
            {
                path: 'home',
                title: '家庭背景',
                name: 'HOME',
                component: resolve => {
                    require(['../views/insona/insona-home.vue'], resolve);
                }
            },
            {
                path: 'document',
                title: '文件服务',
                name: 'DOCUMENT',
                component: resolve => {
                    require(['../views/insona/insona-document.vue'], resolve);
                }
            },
            {
                path: 'type',
                title: '设备类别',
                name: 'TYPE',
                component: resolve => {
                    require(['../views/insona/insona-type.vue'], resolve);
                }
            },
            {
                path: 'product',
                title: '设备出厂',
                name: 'AUTH_PRODUCT',
                access: 'AUTH_PRODUCT',
                component: resolve => {
                    require(['../views/insona/insona-product.vue'], resolve);
                }
            },
            {
                path: 'saleProduct',
                title: '设备销售',
                name: 'AUTH_SALEPRODUCT',
                access: 'AUTH_SALEPRODUCT',
                component: resolve => {
                    require(['../views/insona/insona-saleProduct.vue'], resolve);
                }
            },
            {
                path: 'user-product',
                title: '设备绑定',
                name: 'USER-PRODUCT',
                component: resolve => {
                    require(['../views/insona/insona-user-product.vue'], resolve);
                }
            },
            {
                path: 'users',
                title: '终端用户管理',
                name: 'USERS',
                component: resolve => {
                    require(['../views/insona/insona-users.vue'], resolve);
                }
            },
            {
                path: 'message',
                title: '通知管理',
                name: 'MESSAGE',
                component: resolve => {
                    require(['../views/insona/insona-message.vue'], resolve);
                }
            },
            {
                path: 'material',
                title: '资料管理',
                name: 'MATERIAL',
                component: resolve => {
                    require(['../views/insona/insona-material.vue'], resolve);
                }
            }
        ]
    }
];

// 所有上面定义的路由都要写在下面的routers里
export const routers = [
    loginRouter,
    otherRouter,
    ...appRouter,
    locking,
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
