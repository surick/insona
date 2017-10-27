import ajax from './api';

export default {
    async login(vm, obj) {
        vm.$loading.show();
        ajax(vm, {
            method: 'POST',
            url: '/sys/user/login',
            data: obj
        }).then(res => {
            console.log(res);
        });
        vm.$loading.hide();
    }
};
