import ajax from './api';

export default {
    login(vm, obj) {
        return ajax(vm, {
            method: 'POST',
            url: '/sys/login',
            data: obj
        });
    },

    getPower(vm) {
        return ajax(vm, {
            method: 'GET',
            url: '/sys/power'
        });
    }
};
