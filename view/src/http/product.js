import ajax from './api';
import moment from 'moment';
export default {
    getProducts(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/product/show/' + obj.pageIndex + '/' + obj.pageSize
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
                    });
                    resolve(res);
                }
            });
        });
    },
    addProduct(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/insona/product/save',
            data: {
                did: obj.did,
                name: obj.name,
                serial_code: obj.serial_code,
                version: obj.version,
                sub_inter: obj.sub_inter,
                sub_maker: obj.sub_maker,
                type: obj.type,
                status: '0',
                repair_time: moment(obj.repair_time).format('YYYY-MM-DD')
            }
        });
    },
    deleteProduct(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/insona/product/remove',
            data: ids
        });
    },
    updateProduct(vm, id, obj) {
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/product/update',
            data: {
                id: id,
                did: obj.did,
                name: obj.name,
                serial_code: obj.serial_code,
                version: obj.version,
                sub_inter: obj.sub_inter,
                sub_maker: obj.sub_maker,
                type: obj.type,
                status: obj.status,
                repair_time: moment(obj.repair_time).format('YYYY-MM-DD')
            }
        });
    },
    changeProduct(vm, status, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/product/change/' + status,
            data: {
                ids: obj.ids,
                sub_sale: obj.sub_sale,
                sale_time: moment(obj.sale_time).format('YYYY-MM-DD')
            }
        });
    },
    getDealers(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/product/getDealers'
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
                url: '/insona/product/type'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    getLink(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/product/link'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    }
};
