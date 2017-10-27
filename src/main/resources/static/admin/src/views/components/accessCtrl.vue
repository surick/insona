<template>
    <div v-if="getAccess()">
        <slot></slot>
    </div>
</template>

<script>
    import Cookies from 'js-cookie';
    export default {
        name: 'accessCtrl',

        componentName: 'accessCtrl',

        data () {
            return {
                access: JSON.parse(Cookies.get('access'))
            };
        },

        props: {
            name: String
        },

        methods: {
            getAccess() {
                return this.access[this.name];
            },

            updateAccess() {
                this.access[this.name] = !this.access[this.name];
                Cookies.set('access', JSON.stringify(this.access));
            }
        }
    };
</script>
