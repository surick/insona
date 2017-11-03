import ajax from './api';
import moment from 'moment';

export default {
    login(vm, obj) {
        return ajax(vm, {
            method: 'POST',
            url: '/sys/login',
            data: obj
        });
    },

    getPower(vm) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/sys/power'
            }).then(res => {
                if (res.success) {
                    let array = res.data.menus.concat(res.data.elements);
                    let access = {};
                    array.forEach(item => {
                        access[item.code] = item;
                    });
                    resolve(access);
                }
            });
        });
    },

    getUser(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/sys/user/pageUser/' + obj.pageIndex + '/' + obj.pageSize
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

    addUser(vm, obj) {
        return ajax(vm, {
            method: 'POST',
            url: '/sys/user/addUser',
            data: {
                userName: obj.userName,
                name: obj.name,
                password: obj.password,
                birthday: moment(obj.birthday).format('YYYY-MM-DD'),
                address: obj.address,
                mobilePhone: obj.mobilePhone,
                email: obj.email,
                sex: obj.sex === '男' ? 1 : 0,
                status: obj.status ? 1 : 0
            }
        });
    },

    updateUser(vm, id, obj) {
        return ajax(vm, {
            method: 'PUT',
            url: '/sys/user/updateUser',
            data: {
                id: id,
                name: obj.name,
                birthday: moment(obj.birthday).format('YYYY-MM-DD'),
                address: obj.address,
                mobilePhone: obj.mobilePhone,
                email: obj.email,
                sex: obj.sex === '男' ? 1 : 0,
                status: obj.status ? 1 : 0
            }
        });
    },

    deleteUser(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/sys/user/deleteUser',
            data: ids
        });
    }
};
