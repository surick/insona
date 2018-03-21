import ajax from './api';
import moment from 'moment';

export default {
    getInsonaLog(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'POST',
                url: '/insona/log/getLog/' + obj.pageIndex + '/' + obj.pageSize,
                data: {
                    opt: '关灯',
                    createTime: '',
                    createUser: '',
                    createHost: ''
                }
            }).then(res => {
                if (res.success) {
                    res.data.forEach(item => {
                        item.createTime && (item.createTime = moment(Number(item.createTime)).format('YYYY-MM-DD HH:mm'));
                    });
                    resolve(res);
                }
            });
        });
    }
};