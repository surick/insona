<template>
    <div v-if="getAccess()">
        <slot></slot>
    </div>
</template>

<script>
    export default {
        name: 'accessCtrl',

        componentName: 'accessCtrl',

        data () {
            return {
                access: JSON.parse(localStorage.access || '{}')
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
                localStorage.access = JSON.stringify(this.access);
            }
        }
    };
</script>
