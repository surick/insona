import ajax from './api';
import moment from 'moment';

export default {

    getTable(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/table/listTables'
            }).then(res => {
                if (res.success) {
                    res.data.forEach(item => {
                        item.createTime && (item.createTime = moment(Number(item.createTime)).format('YYYY-MM-DD HH:mm'));
                        item.updateTime && (item.updateTime = moment(Number(item.updateTime)).format('YYYY-MM-DD HH:mm'));
                    });
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
                    res.data.forEach(item => {
                        item.createTime && (item.createTime = moment(Number(item.createTime)).format('YYYY-MM-DD HH:mm'));
                        item.updateTime && (item.updateTime = moment(Number(item.updateTime)).format('YYYY-MM-DD HH:mm'));
                    });
                    resolve(res);
                }
            });
        });
    },
    getMax(vm) {
        return new Promise((resolve) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/table/maxTotal'
            }).then(res => {
                resolve(res);
            });
        });
    }
};
