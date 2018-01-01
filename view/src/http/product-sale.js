import ajax from './api';

export default {
    getList(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/productSale/getList/' + obj.pageIndex + '/' + obj.pageSize
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
                    });
                    resolve(res);
                }
            });
        });
    },
    getPass(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/productSale/getPass/' + obj.pageIndex + '/' + obj.pageSize
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
                    });
                    resolve(res);
                }
            });
        });
    },
    getBack(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/productSale/getBack/' + obj.pageIndex + '/' + obj.pageSize
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
                    });
                    resolve(res);
                }
            });
        });
    },
    passProduct(vm, ids) {
        return ajax(vm, {
            method: 'POST',
            url: '/insona/productSale/pass',
            data: ids
        });
    },
    backProduct(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/productSale/back',
            data: {
                id: obj.id,
                text: obj.text
            }
        });
    },
    updateProduct(vm, id, obj) {
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/type/update',
            data: {
                id: id,
                did: obj.did,
                name: obj.name,
                gizwit_info: obj.gizwit_info,
                insona_online: obj.insona_online,
                serial_code: obj.serial_code,
                last_online: obj.last_online,
                version: obj.version,
                sub_user: obj.sub_user,
                sub_home: obj.sub_home,
                address: obj.address,
                other: obj.other,
                near_status: obj.near_status,
                near_order: obj.near_order,
                near_event: obj.near_event,
                sub_inter: obj.sub_inter,
                sub_maker: obj.sub_maker,
                remark: obj.remark,
                sort_no: obj.sort_no,
                type: obj.type,
                status: obj.status,
                extract: obj.extract
            }
        });
    }
};
