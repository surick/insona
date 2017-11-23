import AccessCtrlComponent from './accessCtrl.vue';

let AccessCtrl = {};
AccessCtrl.install = (Vue) => {
    Vue.component('AccessCtrl', AccessCtrlComponent);
};

export default AccessCtrl;

