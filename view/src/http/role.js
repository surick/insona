import ajax from './api';

export default {
    getRoleTree(vm) {
        return ajax(vm, {
            method: 'GET',
            url: '/sys/role/listAllRoles'
        });
    },

    dealRoleTree(tree) {
        let list = [];
        tree.forEach(item => {
            let obj = {};
            obj.children = [];
            if (item.children && item.children.length > 0) {
                obj.children = this.dealRoleTree(item.children);
            }
            obj.id = item.id;
            obj.title = item.roleName;
            obj.expand = false;
            list.push(obj);
        });
        return list;
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
    }
};
