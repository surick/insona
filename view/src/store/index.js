import Vue from 'vue';
import Vuex from 'vuex';
import { User } from '@/http';
import Main from '@/views/Main.vue';
import { router, otherRouter } from '@/router';
const env = require('@/config/env').default;
const _import = require('@/router/_import_' + env);

Vue.use(Vuex);

// 状态管理
const store = new Vuex.Store({
    state: {
        routers: [otherRouter],
        appRouter: [], // 作为Main组件的子页面展示并且在左侧菜单显示的路由写在appRouter里
        menuList: [],
        tagsList: [...otherRouter.children],
        pageOpenedList: [{
            title: '首页',
            path: '',
            name: 'home'
        }],
        currentPageName: '',
        currentPath: [
            {
                title: '首页',
                path: '',
                name: 'home'
            }
        ],  // 面包屑数组
        openedSubmenuArr: [],  // 要展开的菜单数组
        menuTheme: '', // 主题
        theme: '',
        cachePage: [],
        lang: '',
        isFullScreen: false
    },
    getters: {

    },
    mutations: {
        setTagsList(state, list) {
            state.tagsList.push(...list);
        },
        closePage(state, name) {
            state.cachePage.forEach((item, index) => {
                if (item === name) {
                    state.cachePage.splice(index, 1);
                }
            });
        },
        increateTag(state, tagObj) {
            state.cachePage.push(tagObj.name);
            state.pageOpenedList.push(tagObj);
        },
        initCachepage(state) {
            if (localStorage.pageOpenedList) {
                state.cachePage = JSON.parse(localStorage.pageOpenedList).map(item => {
                    if (item.name !== 'home_index') {
                        return item.name;
                    }
                });
            }
        },
        removeTag(state, name) {
            state.pageOpenedList.map((item, index) => {
                if (item.name === name) {
                    state.pageOpenedList.splice(index, 1);
                }
            });
        },
        pageOpenedList(state, get) {
            let openedPage = state.pageOpenedList[get.index];
            if (get.argu) {
                openedPage.argu = get.argu;
            }
            if (get.query) {
                openedPage.query = get.query;
            }
            state.pageOpenedList.splice(get.index, 1, openedPage);
            localStorage.pageOpenedList = JSON.stringify(state.pageOpenedList);
        },
        clearAllTags(state) {
            state.pageOpenedList.splice(1);
            router.push({
                name: 'home_index'
            });
            state.cachePage = [];
            localStorage.pageOpenedList = JSON.stringify(state.pageOpenedList);
        },
        clearOtherTags(state, vm) {
            let currentName = vm.$route.name;
            let currentIndex = 0;
            state.pageOpenedList.forEach((item, index) => {
                if (item.name === currentName) {
                    currentIndex = index;
                }
            });
            if (currentIndex === 0) {
                state.pageOpenedList.splice(1);
            } else {
                state.pageOpenedList.splice(currentIndex + 1);
                state.pageOpenedList.splice(1, currentIndex - 1);
            }
            let newCachepage = state.cachePage.filter(item => {
                return item === currentName;
            });
            state.cachePage = newCachepage;
            localStorage.pageOpenedList = JSON.stringify(state.pageOpenedList);
        },
        setOpenedList(state) {
            state.pageOpenedList = localStorage.pageOpenedList ? JSON.parse(localStorage.pageOpenedList) : [otherRouter.children[0]];
        },
        setCurrentPath(state, pathArr) {
            state.currentPath = pathArr;
        },
        setCurrentPageName(state, name) {
            state.currentPageName = name;
        },
        addOpenSubmenu(state, name) {
            let hasThisName = false;
            let isEmpty = false;
            if (name.length === 0) {
                isEmpty = true;
            }
            if (state.openedSubmenuArr.indexOf(name) > -1) {
                hasThisName = true;
            }
            if (!hasThisName && !isEmpty) {
                state.openedSubmenuArr.push(name);
            }
        },
        clearOpenedSubmenu(state) {
            state.openedSubmenuArr.length = 0;
        },
        changeMenuTheme(state, theme) {
            state.menuTheme = theme;
        },
        changeMainTheme(state, mainTheme) {
            state.theme = mainTheme;
        },
        lock(state) {
            localStorage.locking = '1';
        },
        unlock(state) {
            localStorage.locking = '0';
        },
        setMenuList(state, menulist) {
            state.menuList = menulist;
        },
        updateMenulist(state) {
            console.log(state.appRouter);
            let accessObj = JSON.parse(localStorage.access || '{}');
            let menuList = [];
            state.appRouter.forEach((item, index) => {
                if (item.access !== undefined) {
                    if (accessObj[item.access]) {
                        if (item.children.length === 1) {
                            menuList.push(item);
                        } else {
                            let len = menuList.push(item);
                            let childrenArr = [];
                            childrenArr = item.children.filter(child => {
                                if (child.access !== undefined) {
                                    if (accessObj[child.access]) {
                                        return child;
                                    }
                                } else {
                                    return child;
                                }
                            });
                            menuList[len - 1].children = childrenArr;
                        }
                    }
                } else {
                    if (item.children.length === 1) {
                        menuList.push(item);
                    } else {
                        let len = menuList.push(item);
                        let childrenArr = [];
                        childrenArr = item.children.filter(child => {
                            if (child.access !== undefined) {
                                if (accessObj[child.access]) {
                                    return child;
                                }
                            } else {
                                return child;
                            }
                        });
                        let handledItem = JSON.parse(JSON.stringify(menuList[len - 1]));
                        handledItem.children = childrenArr;
                        menuList.splice(len - 1, 1, handledItem);
                    }
                }
            });
            state.menuList = menuList;
        },
        setAvator(state, path) {
            localStorage.avatorImgPath = path;
        },
        switchLang(state, lang) {
            state.lang = lang;
            Vue.config.lang = lang;
        },
        handleFullScreen(state) {
            let main = document.getElementById('main');
            if (state.isFullScreen) {
                if (document.exitFullscreen) {
                    document.exitFullscreen();
                } else if (document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if (document.webkitCancelFullScreen) {
                    document.webkitCancelFullScreen();
                } else if (document.msExitFullscreen) {
                    document.msExitFullscreen();
                }
            } else {
                if (main.requestFullscreen) {
                    main.requestFullscreen();
                } else if (main.mozRequestFullScreen) {
                    main.mozRequestFullScreen();
                } else if (main.webkitRequestFullScreen) {
                    main.webkitRequestFullScreen();
                } else if (main.msRequestFullscreen) {
                    main.msRequestFullscreen();
                }
            }
        },
        changeFullScreenState(state) {
            state.isFullScreen = !state.isFullScreen;
        },
        setRoutes(state, routers) {
            state.appRouter = state.appRouter.concat(routers);
            state.routers = [otherRouter, ...state.appRouter];
        }
    },
    actions: {
        GenerateRoutes({ commit }, vm) {
            return new Promise(resolve => {
                User.getPower(vm)
                    .then(res => {
                        if (res.success) {
                            commit('setRoutes', [{
                                path: '/access',
                                icon: 'key',
                                name: 'access',
                                title: '权限管理',
                                component: Main,
                                children: [
                                    {
                                        path: 'index',
                                        title: '权限管理',
                                        name: 'access_index',
                                        component: _import('access/access')
                                    }
                                ]
                            }]);
                            commit('updateMenulist');
                            resolve(res);
                            // localStorage.access = JSON.stringify({test: true, test2: true});
                        }
                    });
            });
        }
    }
});

export default store;
