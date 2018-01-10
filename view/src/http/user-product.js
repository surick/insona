import ajax from './api';

export default {
    getUserProduct(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/userProduct/getUserProduct/' + obj.pageIndex + '/' + obj.pageSize
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
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
                uid: obj.uid
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
    productInfo(vm, did) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/userProduct/getProductInfo/' + did
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    getTypes(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/productSale/getList'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    getUsers(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/userProduct/getUsers'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    }
};
