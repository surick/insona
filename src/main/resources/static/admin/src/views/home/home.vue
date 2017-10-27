<style lang="less">
    @import "./home.less";
    @import "../../styles/common.less";
</style>
<template>
    <div class="home-main">
        <access-ctrl :name="'test2'" ref="access">
            <button>获取用户信息</button>
        </access-ctrl>

        <button @click="ctrlAccess()">控制权限</button>
    </div>
</template>

<script>
    import accessCtrl from '../components/accessCtrl.vue';
    import { User } from '../../http';
    export default {
        name: 'home',

        components: {
            accessCtrl
        },

        mounted() {
            User.login(this, {
                loginName: 'admin',
                password: '123456'
            });
        },

        methods: {
            ctrlAccess() {
                this.$refs.access.updateAccess();
            }
        }
    };
</script>
