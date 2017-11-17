import ajax from './api';
import moment from 'moment';

export default {

    getDocument(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/giz/document/pageDocument/' + obj.pageIndex + '/' + obj.pageSize
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
    addDocument(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/giz/document/saveDocument',
            data: {
                name: obj.name,
                fileUrl: obj.fileUrl,
                fileType: obj.fileType,
                sortNo: obj.sortNo,
                isDelete: 0
            }
        });
    },
    updateDocument(vm, id, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'PUT',
            url: '/giz/document/updateDocument',
            data: {
                id: id,
                name: obj.name,
                fileUrl: obj.fileUrl,
                fileType: obj.fileType,
                sortNo: obj.sortNo,
                isDelete: 0
            }
        });
    },
    deleteDocument(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/giz/document/removeDocument',
            data: ids
        });
    }
};
