import ajax from './api';
import moment from 'moment';

export default {
    getMessage(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/message/listMessage/' + obj.pageIndex + '/' + obj.pageSize
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
                        item.publishTime && (item.publishTime = moment(Number(item.publishTime)).format('YYYY-MM-DD HH:mm'));
                    });
                    resolve(res);
                }
            });
        });
    },
    addMessage(vm, obj) {
        return ajax(vm, {
            method: 'POST',
            url: '/insona/message/saveMessage',
            data: {
                title: obj.title,
                content: obj.content,
                isPublished: obj.is_published ? 1 : 0,
                sortNo: obj.sort_no
            }
        });
    },
    updateMessage(vm, id, obj) {
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/message/updateMessage',
            data: {
                id: id,
                title: obj.title,
                content: obj.content,
                isPublished: obj.is_published ? 1 : 0,
                sortNo: obj.sort_no
            }
        });
    },
    deleteMessage(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/insona/message/removeMessage',
            data: ids
        });
    }
};
