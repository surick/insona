import ajax from './api';

export default {

    getTable(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/table/listTables'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    getMap(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/table/listMap'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    getUser(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/table/getUser'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    putPassword(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'PUT',
                url: '/insona/table/putPassword',
                data: {
                    password: obj.password,
                    newPassword: obj.newPassword
                }
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    }
};
