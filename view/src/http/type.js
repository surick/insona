import ajax from './api';
import moment from 'moment';

export default {
    getTypes(vm, obj) {
        return new Promise((resolve, reject) => {
            ajax(vm, {
                method: 'GET',
                url: '/insona/type/list/' + obj.pageIndex + '/' + obj.pageSize
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
    addType(vm, obj) {
        console.log(obj);
        return ajax(vm, {
            method: 'POST',
            url: '/insona/type/save',
            data: {
                type_id: obj.type_id,
                type_name: obj.type_name,
                maker: obj.maker,
                model_no: obj.model_no,
                make_time: moment(obj.make_time).format('YYYY-MM-DD'),
                appid: obj.appid,
                appsecret: obj.appsecret,
                product_key: obj.product_key,
                into_time: moment(obj.into_time).format('YYYY-MM-DD'),
                technology: obj.technology,
                communication: obj.communication,
                enable: obj.enable ? 1 : 0,
                remark: obj.remark,
                batch: obj.batch,
                is_deleted: 0
            }
        });
    },
    deleteType(vm, ids) {
        return ajax(vm, {
            method: 'DELETE',
            url: '/insona/type/remove',
            data: ids
        });
    },
    updateType(vm, id, obj) {
        return ajax(vm, {
            method: 'PUT',
            url: '/insona/type/update',
            data: {
                id: id,
                type_id: obj.type_id,
                type_name: obj.type_name,
                maker: obj.maker,
                model_no: obj.model_no,
                make_time: moment(obj.make_time).format('YYYY-MM-DD'),
                appid: obj.appid,
                appsecret: obj.appsecret,
                into_time: moment(obj.into_time).format('YYYY-MM-DD'),
                product_key: obj.product_key,
                technology: obj.technology,
                communication: obj.communication,
                enable: obj.enable ? 1 : 0,
                remark: obj.remark,
                batch: obj.batch
            }
        });
    },
    newType(vm, obj) {
        return ajax(vm, {
            method: 'POST',
            url: '/insona/type/new',
            data: {
                type_id: obj.type_id,
                type_name: obj.type_name,
                maker: obj.maker,
                model_no: obj.model_no,
                make_time: moment(obj.make_time).format('YYYY-MM-DD'),
                appid: obj.appid,
                appsecret: obj.appsecret,
                product_key: obj.product_key,
                into_time: obj.into_time,
                person: obj.person,
                technology: obj.technology,
                communication: obj.communication,
                enable: obj.enable ? 1 : 0,
                remark: obj.remark,
                batch: obj.batch,
                is_deleted: 0
            }
        });
    }
};
