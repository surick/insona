import ajax from './api';
import moment from 'moment';

export default {

    getInfo(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/giz/info/pageInfo/' + obj.pageIndex + '/' + obj.pageSize
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
    addInfo(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/giz/info/saveInfo',
            data: {
                title: obj.title,
                infoType: obj.infoType,
                description: obj.description,
                sortNo: obj.sortNo,
                isDelete: 0
            }
        });
    },
    updateInfo(vm, id, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'PUT',
            url: '/giz/info/updateInfo',
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
    deleteInfo(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/giz/info/removeInfo',
            data: ids
        });
    }
};
