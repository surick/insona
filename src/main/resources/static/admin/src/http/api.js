import axios from 'axios';
import env from '../config/env';

const ajaxUrl = env === 'development'
    ? 'http://192.168.3.39:8080'
    : env === 'production'
    ? 'https://www.url.com'
    : 'https://debug.url.com';

const http = axios.create({
    baseURL: ajaxUrl,
    timeout: 30000
});

const ajax = (vm, request) => {
    return new Promise((resolve, reject) => {
        http(request)
            .then(res => {
                if (res.data.code === 200) {
                    resolve(res.data);
                } else {
                    vm.$Message.error(res.data.data.message);
                }
            })
            .catch((err) => {
                if (env === 'development') console.error(err.response);
                vm.$Message.error('网络请求出错！');
            });
    });
};

export default ajax;
