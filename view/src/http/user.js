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
            url: '/sys/user/saveUser',
            data: {
                userName: obj.userName,
                name: obj.name,
                password: obj.password,
                birthday: moment(obj.birthday).format('YYYY-MM-DD'),
                address: obj.address,
                mobilePhone: obj.mobilePhone,
                email: obj.email,
                label: obj.label,
                sex: obj.sex === '男' ? 1 : 0,
                status: obj.status ? 1 : 0,
                type: obj.type,
                qq: obj.qq,
                wechat: obj.wechat,
                phone: obj.phone,
                parentId: obj.parentId
            }
        });
    },
    getParent(vm, id) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/sys/user/parents/' + id
            }).then(res => {
                if (res.success) {
                    resolve(res);
                }
            });
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
                label: obj.label,
                sex: obj.sex === '男' ? 1 : 0,
                status: obj.status ? 1 : 0,
                type: obj.type,
                qq: obj.qq,
                wechat: obj.wechat,
                phone: obj.phone,
                parentId: obj.parentId
            }
        });
    },

    deleteUser(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/sys/user/deleteUser',
            data: ids
        });
    },

    saveRole(vm, obj) {
        return ajax(vm, {
            method: 'PUT',
            headers: {'Access-Control-Allow-Origin': '*'},
            url: '/sys/user/updateRoleUser',
            data: {
                userId: obj.userId,
                roleId: obj.roleId
            }
        });
    },
    dealRoleTreeToList(tree) {
        let list = [];
        tree.forEach(item => {
            let obj = {};
            obj.id = item.id;
            obj.roleName = item.roleName;
            list.push(obj);
            if (item.children && item.children.length > 0) {
                list = list.concat(this.dealRoleTreeToList(item.children));
            }
        });
        return list;
    },
    dealRoleTree(tree) {
        let list = [];
        tree.forEach(item => {
            let obj = {};
            obj.children = [];
            if (item.children && item.children.length > 0) {
                obj.children = this.dealRoleTree(item.children);
            }
            obj.checked = false;
            obj.id = item.id;
            obj.title = item.roleName;
            obj.expand = false;
            list.push(obj);
        });
        return list;
    }
};
