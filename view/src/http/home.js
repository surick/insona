import ajax from './api';
import moment from 'moment';

export default {

    getHome(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/home/pageHome/' + obj.pageIndex + '/' + obj.pageSize
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
    addHome(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/insona/home/saveHome',
            data: {
                title: obj.title,
                infoType: obj.infoType,
                description: obj.description,
                sortNo: obj.sortNo,
                isDelete: 0
            }
        });
    },
    updateHome(vm, id, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/home/updateHome',
            data: {
                id: id,
                title: obj.title,
                infoType: obj.infoType,
                description: obj.description,
                sortNo: obj.sortNo,
                isDelete: 0
            }
        });
    },
    deleteHome(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/insona/home/removeHome',
            data: ids
        });
    }
};
