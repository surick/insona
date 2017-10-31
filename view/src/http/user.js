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
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/sys/power'
            }).then(res => {
                if (res.success) {
                    let array = res.data.menus.concat(res.data.elements);
                    let access = {};
                    array.forEach(item => {
                        access[item.code] = item;
                    });
                    resolve(access);
                }
            });
        });
    }
};
