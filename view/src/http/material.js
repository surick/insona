import ajax from './api';
import moment from 'moment';

export default {
    getMaterial(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/material/listMaterial/' + obj.pageIndex + '/' + obj.pageSize
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
                        item.createTime && (item.createTime = moment(Number(item.createTime)).format('YYYY-MM-DD HH:mm'));
                        item.updateTime && (item.updateTime = moment(Number(item.updateTime)).format('YYYY-MM-DD HH:mm'));
                    });
                    resolve(res);
                }
            });
        });
    },
    addMaterial(vm, obj) {
        return ajax(vm, {
            method: 'POST',
            url: '/insona/material/saveMaterial',
            data: {
                title: obj.title,
                type: obj.type,
                imgUrl: '待上传',
                enabled: 1,
                content: obj.content
            }
        });
    },
    updateMaterial(vm, id, obj) {
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/material/updateMaterial',
            data: {
                id: id,
                title: obj.title,
                type: obj.type,
                imgUrl: obj.imgUrl,
                content: obj.content,
                enabled: obj.enabled ? 1 : 0
            }
        });
    },
    deleteMaterial(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/insona/material/removeMaterial',
            data: ids
        });
    }
};
