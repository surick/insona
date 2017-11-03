import Vue from 'vue';
import iView from 'iview';
import { router, otherRouter, appRouter } from './router';
import store from './store';
import Util from './libs/util';
import App from './app.vue';
import 'iview/dist/styles/iview.css';
import VueI18n from 'vue-i18n';
import Locales from './locale';
import zhLocale from 'iview/src/locale/lang/zh-CN';
import enLocale from 'iview/src/locale/lang/en-US';
import zhTLocale from 'iview/src/locale/lang/zh-TW';
import Loading from './components/loading';
import commonFun from './libs/commonFun';

Vue.use(VueI18n);
Vue.use(iView);
Vue.use(Loading);
Vue.use(commonFun);

// 自动设置语言
const navLang = navigator.language;
const localLang = (navLang === 'zh-CN' || navLang === 'en-US') ? navLang : false;
const lang = window.localStorage.lang || localLang || 'zh-CN';

Vue.config.lang = lang;

// 多语言配置
const locales = Locales;
const mergeZH = Object.assign(zhLocale, locales['zh-CN']);
const mergeEN = Object.assign(enLocale, locales['en-US']);
const mergeTW = Object.assign(zhTLocale, locales['zh-TW']);
Vue.locale('zh-CN', mergeZH);
Vue.locale('en-US', mergeEN);
Vue.locale('zh-TW', mergeTW);

router.beforeEach((to, from, next) => {
    iView.LoadingBar.start();
    Util.title(to.meta.title);
    if (localStorage.token) { // 判断当前是否为登录状态
        if (localStorage.locking === '1' && to.name !== 'locking') { // 判断为锁定状态去非锁定页面
            next(false);
            router.replace({
                name: 'locking'
            });
        } else if (localStorage.locking === '0' && to.name === 'locking') { // 判断非锁定状态去锁定页面
            next(false);
        } else {
            if (to.name !== 'login') { // 正常情况
                let access = Util.getRouterObjByName([otherRouter, ...appRouter], to.name).access;
                if (!store.state.access) {
                    store.dispatch('sysAccess').then((res) => { // 同步用户权限
                        store.commit('updateAccess', res);
                        store.commit('updateMenulist');
                        if (access) {
                            Util.verifyAccess(store.state.access[access], [otherRouter, ...appRouter], to.name, router, next);
                        } else {
                            next();
                        }
                    });
                } else {
                    if (access) {
                        Util.verifyAccess(store.state.access[access], [otherRouter, ...appRouter], to.name, router, next);
                    } else {
                        next();
                    }
                }
            } else { // 判断登录状态去登录页面
                Util.title();
                next({
                    name: 'home'
                });
            }
        }
    } else {
        if (to.name === 'login') {
            next();
        } else {
            router.replace({
                name: 'login'
            });
        }
    }
    iView.LoadingBar.finish();
});

router.afterEach(() => {
    iView.LoadingBar.finish();
});

new Vue({
    el: '#app',
    router: router,
    store: store,
    render: h => h(App),
    data: {
        currentPageName: ''
    },
    mounted () {
        this.currentPageName = this.$route.name;
        this.$store.commit('initCachepage');
        // 权限菜单过滤相关
        this.$store.commit('updateMenulist');
        // 全屏相关
        document.addEventListener('fullscreenchange', () => {
            this.$store.commit('changeFullScreenState');
        });
        document.addEventListener('mozfullscreenchange', () => {
            this.$store.commit('changeFullScreenState');
        });
        document.addEventListener('webkitfullscreenchange', () => {
            this.$store.commit('changeFullScreenState');
        });
        document.addEventListener('msfullscreenchange', () => {
            this.$store.commit('changeFullScreenState');
        });
    },

    created() {
        let tagsList = [];
        appRouter.map((item) => {
            if (item.children.length <= 1) {
                tagsList.push(item.children[0]);
            } else {
                tagsList.push(...item.children);
            }
        });
        this.$store.commit('setTagsList', tagsList);
    }
});
