import axios from 'axios';
import env from '@/config/env';

const ajaxUrl = env === 'development'
    ? 'http://localhost:8080'
    : 'http://api.kyo.hahakeji.com';

const http = axios.create({
    baseURL: ajaxUrl,
    timeout: 30000
});

const ajax = (vm, request) => {
    return new Promise((resolve, reject) => {
        request.headers = { TokenAuthorization: localStorage.token || '' };
        http(request)
            .then(res => {
                switch (res.data.code) {
                case 200:
                    resolve(res.data);
                    break;
                case 401:
                    localStorage.clear();
                    vm && vm.$router.push({
                        name: 'login'
                    });
                    break;
                case 403:
                    vm && vm.$router.push({
                        name: 'error_401'
                    });
                    break;
                case 404:
                    vm && vm.$router.push({
                        name: 'error_404'
                    });
                    break;
                default:
                    vm && vm.$Message.error(res.data.message);
                }
            })
            .catch((err) => {
                if (env === 'development') console.error(err.response);
                vm && vm.$Message.error('网络请求出错！');
                reject(err);
            });
    });
};

export default ajax;
