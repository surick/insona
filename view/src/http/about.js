import ajax from './api';
import moment from 'moment';

export default {
    getAbout(vm, id) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/about/getAbout?id=' + id
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    updateAbout(vm, id, content) {
        return ajax(vm, {
            method: 'POST',
            url: '/insona/about/updateAbout',
            data: {
                id: id,
                content: content
            }
        });
    }
};
