import ajax from './api';
import moment from 'moment';

export default {

    getProduct(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/userProduct/getProduct/' + obj.pageIndex + '/' + obj.pageSize
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
    addProduct(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/insona/userProduct/saveProduct',
            data: {
                did: obj.did,
                baseUserId: obj.uid
            }
        });
    },
    deleteProduct(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/insona/userProduct/removeProduct',
            data: ids
        });
    }
};
