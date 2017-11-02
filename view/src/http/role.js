import ajax from './api';

export default {
    getRoleTree(vm) {
        return ajax(vm, {
            method: 'GET',
            url: '/sys/role/listAllRoles'
        });
    }
};
