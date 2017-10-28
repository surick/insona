import ajax from './api';

export default {
    login(vm, obj) {
        return ajax(vm, {
            method: 'POST',
            url: '/sys/user/login',
            data: obj
        });
    },

    getPower(vm) {
        return ajax(vm, {
            method: 'GET',
            url: '/sys/user/power'
        });
    }
};
