import LoadingComponent from './loading.vue';

let Loading = {};
Loading.install = (Vue) => {
    const LoadingController = Vue.extend(LoadingComponent);
    let instance = new LoadingController().$mount(document.createElement('div'));

    Vue.prototype.$loading = () => {};

    Vue.prototype.$loading['show'] = (message) => {
        instance.message = message;
        document.body.appendChild(instance.$el);
    };

    Vue.prototype.$loading['hide'] = () => {
        document.body.removeChild(instance.$el);
    };
};

export default Loading;
