import ajax from './api';
import moment from 'moment';

export default {

    getDocument(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/document/pageDocument/' + obj.pageIndex + '/' + obj.pageSize
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
    addDocument(vm, obj) {
        return ajax(vm, {
            method: 'POST',
            url: '/insona/document/saveDocument',
            data: {
                id: obj.id,
                name: obj.name,
                fileUrl: obj.fileUrl,
                fileType: obj.fileType,
                sortNo: obj.sortNo,
                isDelete: 0
            }
        });
    },
    updateDocument(vm, id, obj) {
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/document/updateDocument',
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
            url: '/insona/document/removeDocument',
            data: ids
        });
    },
    fileList(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/document/fileList/' + obj.pageIndex + '/' + obj.pageSize + '/' + obj.label
            }).then((res) => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    types(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/type/types'
            }).then((res) => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    }
};
