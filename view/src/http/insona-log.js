import ajax from './api';
import moment from 'moment';

export default {
    getInsonaLog(vm, obj) {
        console.log('==?？' + obj.endTime);
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'POST',
                url: '/insona/log/getLog/' + obj.pageIndex + '/' + obj.pageSize,
                data: {
                    opt: obj.opt,
                    createUser: obj.createUser,
                    startTime: obj.startTime,
                    endTime: obj.endTime
                }
            }).then(res => {
                if (res.success) {
                    res.data.list.forEach(item => {
                        item.createTime && (item.createTime = moment(Number(item.createTime)).format('YYYY-MM-DD HH:mm'));
                    });
                    resolve(res);
                }
            });
        });
    },
    getOperation(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/log/getOperation'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    },
    getUserName(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/log/getUserNameByGroupBy'
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
        });
    }
};