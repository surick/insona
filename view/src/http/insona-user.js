import ajax from './api';
import moment from 'moment';

export default {
    getUsers(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/terminal/listTerminalUser/' + obj.pageIndex + '/' + obj.pageSize
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
                        console.log(Number(item.updateTime));
                        item.createTime && (item.createTime = moment(Number(item.createTime)).format('YYYY-MM-DD HH:mm'));
                        item.updateTime && (item.updateTime = moment(Number(item.updateTime)).format('YYYY-MM-DD HH:mm'));
                    });
                    resolve(res);
                }
            });
        });
    },
    addUserProduct(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/insona/userProduct/saveUserProduct',
            data: {
                did: obj.did,
                baseUserId: obj.uid
            }
        });
    },
    deleteUserProduct(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/insona/userProduct/removeProduct',
            data: ids
        });
    },
    getProducts(vm, uid) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/terminal/getProductById/' + uid
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    userInfo(vm, uid) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/terminal/getUserById/' + uid
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    }
};
