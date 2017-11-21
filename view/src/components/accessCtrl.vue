<template>
    <div class="access-box" v-if="getAccess()">
        <slot></slot>
    </div>
</template>

<script>
    export default {
        name: 'accessCtrl',

        componentName: 'accessCtrl',

        data() {
            return {
                access: this.$store.state.access
            };
        },

        props: {
            name: String
        },

        methods: {
            getAccess() {
                return this.access[this.name];
            }
        }
    };
</script>
<style scoped>
    .access-box {
        display: inline-block;
    }
</style>
