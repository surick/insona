import ajax from './api';

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
                gizwit_info: obj.gizwit_info,
                serial_code: obj.serial_code,
                version: obj.version,
                sub_inter: obj.sub_inter,
                sub_maker: obj.sub_maker,
                type: obj.type,
                status: '0'
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
            url: '/insona/type/update',
            data: {
                id: id,
                did: obj.did,
                name: obj.name,
                gizwit_info: obj.gizwit_info,
                serial_code: obj.serial_code,
                version: obj.version,
                sub_inter: obj.sub_inter,
                sub_maker: obj.sub_maker,
                type: obj.type,
                status: obj.status
            }
        });
    },
    changeProduct(vm, status, ids) {
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/product/change/' + status,
            data: ids
        });
    }
};
