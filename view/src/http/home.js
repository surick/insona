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
    }
};
