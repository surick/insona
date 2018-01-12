import ajax from './api';

export default {
    getRoleTree(vm) {
        return ajax(vm, {
            method: 'GET',
            url: '/sys/role/listAllRoles'
        });
    },
    getAuthTree(vm, id) {
        return ajax(vm, {
            method: 'GET',
            url: '/sys/role/getUserPower/' + id
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
            obj.checked = item.checked;
            obj.selected = item.selected;
            obj.resourceId = item.id;
            obj.title = item.title;
            obj.code = item.code;
            obj.expand = false;
            list.push(obj);
        });
        console.log(list);
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
            obj.code = item.roleCode;
            obj.id = item.id;
            obj.title = item.roleName;
            obj.status = item.enabled;
            obj.parentId = item.parentId;
            obj.expand = false;
            list.push(obj);
        });
        return list;
    },
    saveAuth(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'PUT',
            url: '/sys/role/updateAuthority/'.concat(obj.roleId),
            data: {
                codes: obj.codes
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
                id: obj.id,
                roleName: obj.title,
                parentId: obj.parentId,
                status: obj.status ? 1 : 0,
                roleCode: obj.code
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
