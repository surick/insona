import ajax from './api';

export default {
    getRoleTree(vm) {
        return ajax(vm, {
            method: 'GET',
            url: '/sys/role/listAllRoles'
        });
    },
    getAuthTree(vm) {
        return ajax(vm, {
            method: 'GET',
            url: '/sys/menu/listAllMenus'
        });
    },
    dealAuthTree(tree) {
        let list = [];
        tree.forEach(item => {
            let obj = {};
            obj.children = [];
            if (item.children && item.children.length > 0) {
                obj.children = this.dealAuthTree(item.children);
            }
            obj.resourceId = item.id;
            obj.title = item.title;
            obj.resourceType = item.code;
            obj.expand = false;
            list.push(obj);
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
            obj.id = item.id;
            obj.title = item.roleName;
            obj.expand = false;
            list.push(obj);
        });
        return list;
    },
    saveAuth(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/sys/role/saveAuthority/'.concat(obj.roleId),
            data: {
                MENU: obj.resourceId
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
    updateRole(vm, obj) {
        return ajax(vm, {
            method: 'PUT',
            url: '/sys/role/updateRole',
            data: {
                roleName: obj.roleName,
                code: obj.code,
                parent: obj.parent,
                orderNum: obj.orderNum,
                status: obj.status ? 1 : 0
            }
        });
    },
    addRole(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/sys/role/saveRole',
            data: {
                roleName: obj.roleName,
                roleCode: obj.roleCode,
                parentId: obj.parentId,
                orderNum: obj.orderNum,
                enabled: obj.enabled ? 1 : 0
            }
        });
    },

    deleteRole(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/sys/role/removeRole',
            data: ids
        });
    }

};
