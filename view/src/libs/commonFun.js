let commonFun = {};

commonFun.install = (Vue) => {
    Vue.prototype.$commonFun = () => {};

    Vue.prototype.$commonFun['checkObject'] = function(obj, whiteList) {
        let isEmpty = false;
        for (var key in obj) {
            if (obj[key] === '' && whiteList.indexOf(key) === -1) {
                isEmpty = true;
            }
        }
        return isEmpty;
    };
};

export default commonFun;
